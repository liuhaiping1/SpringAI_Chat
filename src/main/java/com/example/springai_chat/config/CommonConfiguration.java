package com.example.springai_chat.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;


@Configuration
public class CommonConfiguration {
    @Autowired
    private JdbcChatMemoryRepository jdbcChatMemoryRepository;

    @Bean
    public ChatMemory chatMemory() {
        return MessageWindowChatMemory.builder()
                //.chatMemoryRepository(new InMemoryChatMemoryRepository()) //设置存储库，这里是保存到内存（默认）
                .chatMemoryRepository(jdbcChatMemoryRepository)
                .maxMessages(10) //记忆窗口大小（保留最近的10条消息）
                .build();
    }
    @Bean
    public ChatClient chatClient(OllamaChatModel model,ChatMemory chatMemory) {
        return ChatClient.builder(model)
                .defaultSystem("你是一个热心可爱的智能助手，你的名字是小团，请以小团的身份和语气回答问题")
                //MessageChatMemoryAdvisor 的构造方法是私有的，不能直接通过 new 创建实例。
               // .defaultAdvisors(new SimpleLoggerAdvisor() ,new MessageChatMemoryAdvisor(chatMemory))
                .defaultAdvisors(new SimpleLoggerAdvisor(),MessageChatMemoryAdvisor.builder(chatMemory).build())// 正确添加，添加日志记录，方便调试,可以添加多个
                //.defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build()) //添加会话记忆
                .build();
    }

}
