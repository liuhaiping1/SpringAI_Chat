package com.example.springai_chat.config;

import com.example.springai_chat.constants.SystemConstants;
import com.example.springai_chat.tools.CourseTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.redis.RedisVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;




@Configuration
public class CommonConfiguration {
    @Autowired
    private JdbcChatMemoryRepository jdbcChatMemoryRepository;


    /**
     * 会话记忆--数据库存储
     * @return
     */
    @Bean
    public ChatMemory chatMemory() {
        return MessageWindowChatMemory.builder()
                //.chatMemoryRepository(new InMemoryChatMemoryRepository()) //设置存储库，这里是保存到内存（默认）
                .chatMemoryRepository(jdbcChatMemoryRepository)
                .maxMessages(30) //记忆窗口大小（保留最近的10条消息）
                .build();
    }

    // 为gameChatClient创建独立的内存存储
    //每一轮游戏独立，不需要将会话存入数据库中，为gameChatClient创建的存储，存入内存中
    @Bean
    public ChatMemory inMemoryChatMemory() {
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(new InMemoryChatMemoryRepository()) // 使用内存存储，不持久化到数据库
                .maxMessages(30)
                .build();
    }


    @Bean
    public ChatClient chatClient(OpenAiChatModel model,ChatMemory chatMemory) {
        return ChatClient.builder(model)
                .defaultSystem("你是一个热心可爱的智能助手，你的名字是小团，请以小团的身份和语气回答问题")
                //MessageChatMemoryAdvisor 的构造方法是私有的，不能直接通过 new 创建实例。
               // .defaultAdvisors(new SimpleLoggerAdvisor() ,new MessageChatMemoryAdvisor(chatMemory))
                .defaultAdvisors(new SimpleLoggerAdvisor(),MessageChatMemoryAdvisor.builder(chatMemory).build())// 正确添加，添加日志记录，方便调试,可以添加多个
                //.defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build()) //添加会话记忆
                .build();
    }

    @Bean
    public ChatClient gameChatClient(OpenAiChatModel model, ChatMemory inMemoryChatMemory) {
        return ChatClient.builder(model)
                .defaultSystem(SystemConstants.GAME_SYSTEM_PROMPT)
                .defaultAdvisors(new SimpleLoggerAdvisor(),MessageChatMemoryAdvisor.builder(inMemoryChatMemory).build())
                .build();
    }



    @Bean
    public ChatClient serviceChatClient(OpenAiChatModel model, ChatMemory inMemoryChatMemory, CourseTools courseTools) {
        return ChatClient.builder(model)
                .defaultSystem(SystemConstants.SERVICE_SYSTEM_PROMPT)
                .defaultAdvisors(new SimpleLoggerAdvisor(),
                        MessageChatMemoryAdvisor.builder(inMemoryChatMemory).build())
                .defaultTools(courseTools)
                .build();
    }



}