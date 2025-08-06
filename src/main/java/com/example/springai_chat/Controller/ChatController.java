package com.example.springai_chat.Controller;




import com.example.springai_chat.entity.vo.Result;
import com.example.springai_chat.mapper.ChatHistoryMapper;
import com.example.springai_chat.mapper.SpringAiChatMemoryMapper;
import com.example.springai_chat.repository.ChatHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.content.Media;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/ai")
public class ChatController {
    //lombok,@RequiredArgsConstructor，构造方法注入ChatClient
    private final ChatClient chatClient;
    @Qualifier("mutilChatClient")
    private final ChatClient mutilChatClient;

    private final ChatHistoryRepository inMemoryChatHistoryRepository;

    private final ChatHistoryRepository inSqlChatHistoryRepository;

    private final SpringAiChatMemoryMapper springAiChatMemoryMapper;
    private final ChatHistoryMapper chatHistoryMapper;
//    @RequestMapping("/chat")
//    public String chat(@RequestParam("prompt") String prompt) {
//        return chatClient.prompt()
//                .user(prompt)
//                .call()
//                .content();
//    }


    /**
     * 流式调用AI聊天接口
     * 
     * 该方法使用流式响应的方式与AI进行对话，支持对话记忆功能。
     * 通过设置produces为"text/html;charset=utf-8"确保中文不乱码
     * 
     * @param prompt 用户输入的提示词/问题内容
     * @param chatId 对话ID，用于标识和维持同一会话的上下文记忆
     * @return Flux<String> 流式响应的AI回复内容，每个元素为部分响应文本
     */
    @RequestMapping(value = "/chat",produces = "text/html;charset=utf-8") //流式输出，需要设置输出的格式，会输出乱码
    public Flux<String> chat(@RequestParam("prompt") String prompt,
                             @RequestParam("chatId") String chatId,
                             @RequestParam(value = "files", required = false)List<MultipartFile> files) {
        //保存会话id
//        inMemoryChatHistoryRepository.save("chat",chatId);
        inSqlChatHistoryRepository.save("chat",chatId);
        //请求模型
        if(files == null || files.isEmpty()){
            //没有附件，纯文本聊天
            log.info("纯文本聊天-----------");
            return textChat(prompt,chatId);
        }else {
            //有附件，多模态聊天
            log.info("多模态聊天-----------");
            return multiModalChat(prompt,chatId,files);
        }

    }

    private Flux<String> multiModalChat(String prompt, String chatId, List<MultipartFile> files) {
        //1.解析多媒体.文件转为media
        List<Media> medias = files.stream()
                .map(file -> new Media(
                        MimeType.valueOf(file.getContentType()),
                        file.getResource()
                    )
                ).toList();
        //2.请求模型，多模态聊天
        return mutilChatClient.prompt()
                .user(p -> p.text(prompt).media(medias.toArray(Media[]::new)))// 设置用户输入
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, chatId)) //设置会话ID
                .stream()
                .content();
    }

    private Flux<String> textChat(String prompt, String chatId) {
        return chatClient.prompt()
                .user(prompt)// 设置用户输入
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, chatId)) //设置会话ID
                .stream()
                .content();
    }


    @DeleteMapping("/{type}/{chatId}")
    public ResponseEntity<Result> delete(@PathVariable String type, @PathVariable String chatId) {
        log.info("开始处理删除PDF请求，chatId: {}", chatId);

        try {
            springAiChatMemoryMapper.deleteByChatId(chatId);
            chatHistoryMapper.deleteByTypeAndChatId(type, chatId);
            return ResponseEntity.ok(Result.ok());
        } catch (Exception e) {
            log.error("删除失败, chatId: {}", chatId, e);
            return ResponseEntity.status(500).body(Result.fail("删除失败: " + e.getMessage()));
        }
    }
}