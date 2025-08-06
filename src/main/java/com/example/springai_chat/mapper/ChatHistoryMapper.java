package com.example.springai_chat.mapper;

import com.example.springai_chat.entity.po.ChatHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Delete;
import java.util.List;

@Mapper
public interface ChatHistoryMapper {
    
    @Insert("INSERT INTO chat_history(type, chat_id) VALUES(#{type}, #{chatId})")
    int insert(ChatHistory chatHistory);
    
    @Select("SELECT chat_id FROM chat_history WHERE type = #{type}")
    List<String> selectChatIdsByType(String type);
    
    @Select("SELECT COUNT(*) FROM chat_history WHERE type = #{type} AND chat_id = #{chatId}")
    int countByTypeAndChatId(String type, String chatId);
    
    @Delete("DELETE FROM chat_history WHERE chat_id = #{chatId}")
    int deleteByTypeAndChatId(String type, String chatId);
}
