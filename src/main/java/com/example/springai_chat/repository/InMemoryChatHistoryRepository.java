package com.example.springai_chat.repository;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 内存实现的聊天记录仓库
 * 
 * 该类使用内存中的HashMap来存储和管理不同业务类型的会话ID列表。
 * 作为Spring组件，在应用重启后数据会丢失，适用于开发测试环境或临时数据存储。
 * 每个业务类型对应一个会话ID列表，支持快速查找和去重功能。
 */
@Repository("inMemoryChatHistoryRepository")
public class InMemoryChatHistoryRepository implements ChatHistoryRepository {

    /**
     * 聊天记录存储结构
     * 
     * key: 业务类型（如chat、service、pdf等）
     * value: 该业务类型下的所有会话ID列表
     */
    private final Map<String, List<String>> chatHistory = new HashMap<>();
    /**
     * 保存会话ID到指定业务类型的聊天记录中
     * 
     * 将新的会话ID添加到对应业务类型的会话列表中。
     * 如果该业务类型不存在，会自动创建新的列表。
     * 具有去重功能，避免重复添加相同的会话ID。
     * 
     * @param type 业务类型，用于分类存储会话ID（如chat、service、pdf等）
     * @param chatId 会话唯一标识符，将被添加到对应业务类型的会话列表中
     */
    @Override
    public void save(String type, String chatId) {
        // 如果该业务类型不存在，创建新的会话ID列表
/*        if (!chatHistory.containsKey(type)) {
            chatHistory.put(type,new ArrayList<>());
        }
        // 获取该业务类型的会话ID列表d
        List<String> chatIds = chatHistory.get(type);
        以上代码可以简化为下面一行*/
        List<String> chatIds = chatHistory.computeIfAbsent(type, k -> new ArrayList<>());

        // 去重检查：如果会话ID已存在，直接返回避免重复添加
        if (chatIds.contains(chatId)){
           return;
        }
        
        // 添加新的会话ID到列表中
        chatIds.add(chatId);
    }

    /**
     * 获取指定业务类型的所有会话ID
     * 
     * 根据业务类型查询对应的会话ID列表。
     * 如果该业务类型不存在或没有会话记录，返回空列表而非null，
     * 确保调用方无需进行null检查，提高代码健壮性。
     * 
     * @param type 业务类型，用于查询对应的会话ID列表
     * @return List<String> 该业务类型下的所有会话ID列表，按添加顺序排列。如果无记录返回空列表
     */
    @Override
    public List<String> getChatIds(String type) {
        /*// 从内存中获取指定业务类型的会话ID列表
        List<String> chatIds = chatHistory.get(type);

        // 防御式编程：返回空列表避免NullPointerException
        return chatIds == null ? new ArrayList<>() : chatIds;*/

        return chatHistory.getOrDefault(type, new ArrayList<>());
    }

    @Override
    public void delete(String type, String chatId) {
        // 获取指定业务类型的会话ID列表
        List<String> chatIds = chatHistory.get(type);
        // 如果列表存在且包含指定会话ID，则移除该ID
        if (chatIds != null){
            chatIds.remove(chatId);
        }
    }


}