package com.example.springai_chat.entity;

import java.time.LocalDateTime;

public class ChatDocument {
    private Long id;
    private String chatId;
    private String fileUrl;
    private LocalDateTime createdTime;

    // 构造函数
    public ChatDocument() {}

    public ChatDocument(String chatId, String fileUrl) {
        this.chatId = chatId;
        this.fileUrl = fileUrl;
    }

    // Getter和Setter方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }
}