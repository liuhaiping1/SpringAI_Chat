package com.example.springai_chat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.example.springai_chat.mapper")
@SpringBootApplication
public class SpringaiChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringaiChatApplication.class, args);
    }

}
