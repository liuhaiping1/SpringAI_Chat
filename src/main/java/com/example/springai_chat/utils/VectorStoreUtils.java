package com.example.springai_chat.utils;


import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.util.List;

/**
 * 文件向量化工具类
 */
@Slf4j
@Component
public class VectorStoreUtils {

    @Autowired
    private VectorStore vectorStore;

    /**
     * 将文档向量化，并添加到Redis向量库
     */
    public void redisVectorStore(String  fileUrl,String chatId) throws MalformedURLException {
        Resource resource = new UrlResource(fileUrl);
        // 1.创建PDF的读取器
        PagePdfDocumentReader reader = new PagePdfDocumentReader(
                resource, // 文件源
                PdfDocumentReaderConfig.builder()
                        .withPageExtractedTextFormatter(ExtractedTextFormatter.defaults())
                        .withPagesPerDocument(1) // 每1页PDF作为一个Document
                        .build()
        );
        // 2.读取PDF文档，拆分为Document
        List<Document> documents = reader.read();


        // 3.为每个文档添加chatId元数据
        for (Document document : documents) {
            document.getMetadata().put("chatId", chatId);
        }

        // 4.写入向量库
        int batchSize = 10;  // 批量写入向量库的批次大小

        for (int i = 0; i < documents.size(); i += batchSize) {
            int endIndex = Math.min(i + batchSize, documents.size());
            List<Document> batch = documents.subList(i, endIndex);

            // 向量化这一批文档
            vectorStore.add(batch);
        }

    }

    /**
     * 根据chatId删除Redis向量数据库中的相关数据
     * @param chatId 聊天ID
     */
    public void deleteVectorDataByChatId(String chatId) {
        try {
            // 使用过滤表达式删除指定chatId的文档
            vectorStore.delete("chatId == '" + chatId + "'");
            log.info("成功删除chatId为 {} 的向量数据", chatId);
        } catch (Exception e) {
            log.error("删除向量数据失败，chatId: {}", chatId, e);
            throw new RuntimeException("删除向量数据失败: " + e.getMessage(), e);
        }
    }
}
