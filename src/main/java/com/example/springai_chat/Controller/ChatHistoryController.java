package com.example.springai_chat.Controller;


import com.example.springai_chat.entity.vo.MessageVO;
import com.example.springai_chat.repository.ChatHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ai/history")
@RequiredArgsConstructor
public class ChatHistoryController {
    // 使用内存存储
    private final ChatHistoryRepository inMemoryChatHistoryRepository;
    // 使用数据库存储
    private final ChatHistoryRepository inSqlChatHistoryRepository;

    private final ChatMemory chatMemory;

    /**
     * 根据业务类型获取会话Id
     * @param type 业务类型，如：chat,service,pdf
     * @return 该业务类型下的所有会话ID列表
     */
    @GetMapping("/{type}")
    public List<String> getChatIds(@PathVariable("type") String type) {
        return inSqlChatHistoryRepository.getChatIds(type);
    }


    /**
     * 获取指定会话的完整聊天记录
     * 
     * 根据业务类型和会话ID，从内存中获取该会话的所有消息记录。
     * 如果找不到对应的聊天记录，返回空列表而不是null，确保前端处理的一致性。
     * 
     * @param type 业务类型，用于区分不同场景的聊天记录（如：chat-普通聊天、service-客服对话、pdf-PDF问答等）
     * @param chatId 会话唯一标识符，用于定位具体的聊天记录
     * @return List<MessageVO> 该会话的所有消息列表，按时间顺序排列。如果无记录则返回空列表
     */
    @GetMapping("/{type}/{chatId}")
    public List<MessageVO> getChatHistory(@PathVariable("type") String type, @PathVariable("chatId") String chatId) {
        // 从ChatMemory中获取指定chatId的所有消息
        List<Message> messages = chatMemory.get(chatId);
        if (messages == null || messages.isEmpty()){
            // 如果聊天记录为空，返回空列表避免NullPointerException
            return List.of();
        }
        // 将Message实体转换为MessageVO视图对象，便于前端展示
        return messages.stream().map(MessageVO::new).toList();
    }
    

}