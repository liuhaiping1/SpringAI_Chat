package com.example.springai_chat.repository;

import java.util.List;

public interface ChatHistoryRepository {
    /**
     * 保存业务类型和会话ID
     * @param type 业务类型，如：chat,service,pdf
     * @param chatId  会话ID
     */
    void save(String type,String chatId);

    /**
     * 根据业务类型查询所有会话ID
     * @param type 业务类型，如：chat,service,pdf
     * @return 该业务类型下的所有会话ID列表
     */
    List<String> getChatIds(String type);

    /**
     * 根据业务类型和会话ID删除会话记录
     * @param type 业务类型，如：chat,service,pdf
     * @param chatId  会话ID
     */
    void delete(String type,String chatId);

}
