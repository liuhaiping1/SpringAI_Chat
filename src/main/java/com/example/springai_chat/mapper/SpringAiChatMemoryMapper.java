package com.example.springai_chat.mapper;

import org.apache.ibatis.annotations.Delete;

public interface SpringAiChatMemoryMapper {
    @Delete("delete from spring_ai_chat_memory where conversation_id = #{chatId}")
    void deleteByChatId(String chatId);
}
