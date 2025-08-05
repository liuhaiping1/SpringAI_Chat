package com.example.springai_chat.Controller;

import com.example.springai_chat.entity.vo.Result;
import com.example.springai_chat.repository.ChatHistoryRepository;
import com.example.springai_chat.repository.uploadPdfFileRepository;
import com.example.springai_chat.service.ChatDocumentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/ai/pdf")
@RequiredArgsConstructor
@Slf4j
public class ChatPdfController {


    private final ChatClient pdfChatClient;
    private final ChatHistoryRepository inMemoryChatHistoryRepository;
    private final uploadPdfFileRepository uploadPdfFileRepository;
    private final ChatDocumentService chatDocumentService;
    
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
    @DeleteMapping("/documents/{id}")
    public ResponseEntity<Result> deleteDocument(@PathVariable Long id) {
        try {
            chatDocumentService.deleteDocument(id);
            return ResponseEntity.ok(Result.ok());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Result.fail("文档删除失败: " + e.getMessage()));
        }
    }


    @RequestMapping(value = "/chat",produces = "text/html;charset=utf-8") //流式输出，需要设置输出的格式，会输出乱码
    public Flux<String> chat(@RequestParam("prompt") String prompt, @RequestParam("chatId") String chatId) {
        //保存会话id
        inMemoryChatHistoryRepository.save("chat",chatId);
        //inSqlChatHistoryRepository.save("chat",chatId);
        //请求模型
        return pdfChatClient.prompt()
                .user(prompt)// 设置用户输入
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, chatId)) //设置会话ID
                .stream()
                .content();
    }
}