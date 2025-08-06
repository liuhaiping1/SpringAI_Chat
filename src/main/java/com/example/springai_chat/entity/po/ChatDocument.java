package com.example.springai_chat.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatDocument {
    private Long id;
    private String chatId;
    private String fileUrl;
    private String fileName;
    private LocalDateTime createdTime;


    public ChatDocument(String chatId, String fileUrl, String fileName) {
        this.chatId = chatId;
        this.fileUrl = fileUrl;
        this.fileName = fileName;
    }

}