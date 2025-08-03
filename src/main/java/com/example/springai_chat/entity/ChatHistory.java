package com.example.springai_chat.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatHistory {
    private String Id;
    private String type;
    private String chatId;
}
