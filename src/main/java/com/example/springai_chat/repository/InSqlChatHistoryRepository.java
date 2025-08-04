package com.example.springai_chat.repository;

import com.example.springai_chat.mapper.ChatHistoryMapper;
import com.example.springai_chat.entity.po.ChatHistory;
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
        // 检查是否已存在，避免重复插入
        if (!exists(type, chatId)) {

            ChatHistory chatHistory = new ChatHistory();
            chatHistory.setType(type);
            chatHistory.setChatId(chatId);

            chatHistoryMapper.insert(chatHistory);
        }
    }

    private boolean exists(String type, String chatId) {
        return chatHistoryMapper.countByTypeAndChatId(type, chatId) > 0;
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
        chatHistoryMapper.deleteByTypeAndChatId(type, chatId);
    }
}
