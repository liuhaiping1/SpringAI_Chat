package com.example.springai_chat.Mapper;


import com.example.springai_chat.entity.ChatHistory;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ChatHistoryMapper {

    /**
     * 插入一条聊天记录的type和chatId
     * @param chatHistory
     */
    @Insert("insert into chat_history (type, chat_id) values (#{type}, #{chatId})")
    void insert(ChatHistory chatHistory);

    /**
     * 删除指定类型和会话ID的聊天记录
     * @param type 业务类型
     * @param chatId 会话ID
     */
    @Delete("delete from chat_history where type = #{type} and chat_id = #{chatId}")
    void delete(String type, String chatId);

    /**
     * 根据业务类型查询聊天记录Ids
     * @param type 业务类型
     * @return 聊天记录列表
     */
    @Select("select chat_id from chat_history where type = #{type}")
    List<String> selectChatIdsByType(String type);
}
