package com.example.springai_chat.utils;


import com.example.springai_chat.entity.ChatDocument;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.util.List;

/**
 * 文件向量化工具类
 */
@Component
public class VectorStoreUtils {

    @Autowired
    private VectorStore vectorStore;

    /**
     * 将文档向量化，并添加到Redis向量库
     */
    public void redisVectorStore(String  fileUrl) throws MalformedURLException {
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

        // 3.写入向量库
        int batchSize = 10;  // 批量写入向量库的批次大小

        for (int i = 0; i < documents.size(); i += batchSize) {
            int endIndex = Math.min(i + batchSize, documents.size());
            List<Document> batch = documents.subList(i, endIndex);

            // 向量化这一批文档
            vectorStore.add(batch);
        }

    }
}
