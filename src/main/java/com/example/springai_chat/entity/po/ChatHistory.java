package com.example.springai_chat.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatHistory {
    private Long id;
    private String type;
    private String chatId;

}
