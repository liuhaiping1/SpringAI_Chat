package com.example.springai_chat.Controller;




import com.example.springai_chat.repository.ChatHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;


@RequiredArgsConstructor
@RestController
@RequestMapping("/ai")
public class ChatController {
    //lombok,@RequiredArgsConstructor，构造方法注入ChatClient
    private final ChatClient chatClient;

    private final ChatHistoryRepository inMemoryChatHistoryRepository;

    private final ChatHistoryRepository inSqlChatHistoryRepository;
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
    public Flux<String> chat(@RequestParam("prompt") String prompt,@RequestParam("chatId") String chatId) {
        //保存会话id
//        inMemoryChatHistoryRepository.save("chat",chatId);
        inSqlChatHistoryRepository.save("chat",chatId);
        //请求模型
        return chatClient.prompt()
                .user(prompt)// 设置用户输入
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, chatId)) //设置会话ID
                .stream()
                .content();
    }
}