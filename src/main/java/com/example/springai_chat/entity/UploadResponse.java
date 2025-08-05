package com.example.springai_chat.entity;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadResponse {
    private String chatId;
    private String fileUrl;
    private String fileName;
    private String message;
    
    // 添加便于使用的构造函数
    public UploadResponse(boolean success, String message, String fileUrl) {
        this.message = message;
        this.fileUrl = fileUrl;
    }
}