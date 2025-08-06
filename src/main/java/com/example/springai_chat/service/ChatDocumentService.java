package com.example.springai_chat.service;

import com.example.springai_chat.entity.po.ChatDocument;
import com.example.springai_chat.mapper.ChatDocumentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatDocumentService {
    
    @Autowired
    private ChatDocumentMapper chatDocumentMapper;
    
    /**
     * 保存文档信息
     */
    public void saveDocument(String chatId, String fileUrl, String fileName) {
        ChatDocument document = new ChatDocument(chatId, fileUrl, fileName);
        chatDocumentMapper.insert(document);
    }
    
    /**
     * 根据chatId获取文档列表
     */
    public ChatDocument getDocumentsByChatId(String chatId) {
        return chatDocumentMapper.findByChatId(chatId);
    }

    /**
     * 根据chatId获取文件URL
     * @param chatId
     * @return
     */
    public String getFileURLByChatId(String chatId){
        ChatDocument document = chatDocumentMapper.findByChatId(chatId);
        if (document == null) {
            return null;
        }
        return document.getFileUrl();
    }
    
    /**
     * 删除文档
     */
    public void deleteDocument(Long id) {
        chatDocumentMapper.deleteById(id);
    }
    
    /**
     * 删除某个聊天会话的所有文档
     */
    public void deleteDocumentsByChatId(String chatId) {
        chatDocumentMapper.deleteByChatId(chatId);
    }
}