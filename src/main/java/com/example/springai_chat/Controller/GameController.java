package com.example.springai_chat.Controller;

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
public class GameController {
    //lombok,@RequiredArgsConstructor，构造方法注入ChatClient
    private final ChatClient gameChatClient;


    @RequestMapping(value = "/game",produces = "text/html;charset=utf-8") //流式输出，需要设置输出的格式，会输出乱码
    public Flux<String> chat(@RequestParam("prompt") String prompt, @RequestParam("chatId") String chatId) {

        //请求模型
        return gameChatClient.prompt()
                .user(prompt)// 设置用户输入
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, chatId)) //设置会话ID
                .stream()
                .content();
    }

}
