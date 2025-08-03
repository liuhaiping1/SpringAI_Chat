package com.example.springai_chat.repository;

import com.example.springai_chat.Mapper.ChatHistoryMapper;
import com.example.springai_chat.entity.ChatHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository("inSqlChatHistoryRepository")
public class InSqlChatHistoryRepository implements ChatHistoryRepository{

    private final ChatHistoryMapper chatHistoryMapper;

    /**
     * 保存业务类型和会话ID
     * @param type 业务类型，如：chat,service,pdf
     * @param chatId  会话ID
     */
    @Override
    public void save(String type, String chatId) {
        // 先查询是否已存在
        if (exists(type, chatId)) return;

        ChatHistory chatHistory = new ChatHistory();
        chatHistory.setType(type);
        chatHistory.setChatId(chatId);
        chatHistoryMapper.insert(chatHistory);
    }

    // 判断 chatId 是否已存在
    private boolean exists(String type, String chatId) {
        List<String> chatIds = chatHistoryMapper.selectChatIdsByType(type);
        return chatIds.contains(chatId);
    }

    /**
     * 根据业务获取所有 chatId
     * @param type 业务类型，如：chat,service,pdf
     * @return 该业务类型下的所有会话ID列表
     */
    @Override
    public List<String> getChatIds(String type) {
        return chatHistoryMapper.selectChatIdsByType(type);
    }

    /**
     * 删除某个会话
     * @param type 业务类型，如：chat,service,pdf
     * @param chatId 会话ID
     */
    @Override
    public void delete(String type, String chatId) {
        chatHistoryMapper.delete(type,chatId);
    }
}
