package com.example.springai_chat.Controller;

import com.example.springai_chat.entity.po.ChatDocument;
import com.example.springai_chat.entity.vo.Result;
import com.example.springai_chat.mapper.ChatHistoryMapper;
import com.example.springai_chat.mapper.SpringAiChatMemoryMapper;
import com.example.springai_chat.repository.UploadPdfFileRepository;
import com.example.springai_chat.service.ChatDocumentService;
import com.example.springai_chat.utils.OssUtil;
import com.example.springai_chat.utils.VectorStoreUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/ai/pdf")
@RequiredArgsConstructor
@Slf4j
public class ChatPdfController {
    private final VectorStore redisVectorStore; // 添加VectorStore注入


    private final ChatClient pdfChatClient;
    private final UploadPdfFileRepository uploadPdfFileRepository;
    private final ChatDocumentService chatDocumentService;
    private final ChatHistoryMapper chatHistoryMapper;
    private final SpringAiChatMemoryMapper springAiChatMemoryMapper;
    private final OssUtil ossUtil;
    private final VectorStoreUtils vectorStoreUtils;
    
    /**
     * 上传文件到OSS - 路径变量为chatId
     */
    @PostMapping("/upload/{chatId}")
    public ResponseEntity<Result> uploadFile(
            @RequestParam("file") MultipartFile file,
            @PathVariable("chatId") String chatId) {
        
        log.info("接收到上传请求，chatId: {}", chatId);
        
        try {
            // 调用repository的save方法，一次性完成上传和保存
            boolean success = uploadPdfFileRepository.save(chatId, file);
            
            if (success) {
                return ResponseEntity.ok(Result.ok());
            } else {
                return ResponseEntity.status(500).body(Result.fail("文件上传失败"));
            }
            
        } catch (Exception e) {
            log.error("文件上传失败", e);
            return ResponseEntity.status(500).body(Result.fail("文件上传失败: " + e.getMessage()));
        }
    }

    
    /**
     * 删除文档
     */
    @DeleteMapping("/documents/{chatId}")
    public ResponseEntity<Result> deleteDocument(@PathVariable Long chatId) {
        try {
            chatDocumentService.deleteDocument(chatId);
            return ResponseEntity.ok(Result.ok());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Result.fail("文档删除失败: " + e.getMessage()));
        }
    }


    @RequestMapping(value = "/file/{chatId}")
    public ResponseEntity<Resource> getFile(@PathVariable() String chatId) {
        try {
            // 获取文档信息
            ChatDocument document = chatDocumentService.getDocumentsByChatId(chatId);
            if (document == null) {
                log.warn("未找到chatId对应的文档: {}", chatId);
                return ResponseEntity.notFound().build();
            }
            
            String fileUrl = document.getFileUrl();
            log.info("获取文档URL: {}", fileUrl);
            String fileName = document.getFileName();
            
            if (fileUrl == null) {
                log.warn("文档URL为空, chatId: {}", chatId);
                return ResponseEntity.notFound().build();
            }
            
            // 从OSS下载文件
            URL url = new URL(fileUrl);
            InputStream inputStream = url.openStream();
            
            // 使用数据库中存储的文件名，如果为空则从URL提取
            if (fileName == null || fileName.trim().isEmpty()) {
                fileName = extractFilenameFromUrl(fileUrl);
            }
            
            // 创建Resource
            Resource resource = new InputStreamResource(inputStream);
            
            // 对文件名进行URL编码处理
            String encodedFileName;
            try {
                encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString())
                        .replaceAll("\\+", "%20");
            } catch (UnsupportedEncodingException e) {
                encodedFileName = "document.pdf";
            }
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, 
                            "attachment; filename*=UTF-8''" + encodedFileName)
                    .header(HttpHeaders.CONTENT_TYPE, "application/pdf")
                    .body(resource);
                    
        } catch (Exception e) {
            log.error("获取PDF文件失败, chatId: {}", chatId, e);
            return ResponseEntity.status(500).build();
        }
    }

    private String extractFilenameFromUrl(String fileUrl) {
        // 从URL中提取文件名的逻辑
        String[] parts = fileUrl.split("/");
        return parts[parts.length - 1];
    }


    @RequestMapping(value = "/chat",produces = "text/html;charset=utf-8")
    public Flux<String> chat(@RequestParam("prompt") String prompt, @RequestParam("chatId") String chatId) {
        log.info("开始处理PDF聊天请求，chatId: {}, prompt: {}", chatId, prompt);


        SearchRequest searchRequest = SearchRequest.builder()
                .query(prompt)
                .similarityThreshold(0.7)
                .topK(3)
                .filterExpression("chatId == '" + chatId + "'")
                .build();

        // 测试带过滤条件的搜索
        List<Document> filteredDocs = redisVectorStore.similaritySearch(searchRequest);

        log.info("执行相似性搜索，过滤条件：{}", searchRequest.getFilterExpression());
        log.info("带过滤条件的搜索结果数量: {}", filteredDocs.size());
        log.info("文档元数据: {}", filteredDocs.get(0).getMetadata());
        // 动态构建QuestionAnswerAdvisor
        QuestionAnswerAdvisor questionAnswerAdvisor = QuestionAnswerAdvisor.builder(redisVectorStore)
                .searchRequest(searchRequest)
                .build();
        
        // 请求模型，动态添加advisor
        return pdfChatClient.prompt()
                .user(prompt)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, chatId)
                        .param("chatId", chatId))
                .advisors(questionAnswerAdvisor)
                .stream()
                .content();
    }


    @DeleteMapping("/{type}/{chatId}")
    public ResponseEntity<Result> delete(@PathVariable String type, @PathVariable String chatId) {
        log.info("开始处理删除PDF请求，chatId: {}", chatId);

        try {
            vectorStoreUtils.deleteVectorDataByChatId(chatId);
            ossUtil.deleteFileByUrl(chatDocumentService.getFileURLByChatId(chatId));
            springAiChatMemoryMapper.deleteByChatId(chatId);
            chatHistoryMapper.deleteByTypeAndChatId(type, chatId);
            chatDocumentService.deleteDocumentsByChatId(chatId);
            return ResponseEntity.ok(Result.ok());
        } catch (Exception e) {
            log.error("删除PDF文件失败, chatId: {}", chatId, e);
            return ResponseEntity.status(500).body(Result.fail("删除PDF文件失败: " + e.getMessage()));
        }
    }
}
