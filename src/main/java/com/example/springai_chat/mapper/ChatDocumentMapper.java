package com.example.springai_chat.mapper;

import com.example.springai_chat.entity.po.ChatDocument;
import org.apache.ibatis.annotations.*;

@Mapper
public interface ChatDocumentMapper {
    
    @Insert("INSERT INTO chat_document (chat_id, file_url, file_name) VALUES (#{chatId}, #{fileUrl}, #{fileName})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(ChatDocument chatDocument);
    
    @Select("SELECT * FROM chat_document WHERE chat_id = #{chatId}")
    ChatDocument findByChatId(String chatId);
    
    @Delete("DELETE FROM chat_document WHERE id = #{id}")
    int deleteById(Long id);
    
    @Delete("DELETE FROM chat_document WHERE chat_id = #{chatId}")
    int deleteByChatId(String chatId);
}