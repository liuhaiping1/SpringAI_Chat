package com.example.springai_chat.repository;

import com.example.springai_chat.entity.ChatDocument;
import com.example.springai_chat.service.ChatDocumentService;
import com.example.springai_chat.utils.OssUtil;
import com.example.springai_chat.utils.VectorStoreUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class uploadPdfFileRepository implements FileRepository {

    private final VectorStoreUtils vectorStoreUtils;
    private final OssUtil ossUtil;
    private final ChatDocumentService chatDocumentService;

    
    /**
     * 上传文件到OSS，并且保存chatId与URL到数据库中
     * @param chatId 会话id
     * @param file MultipartFile文件
     * @return 上传成功返回true，失败返回false
     */
    public boolean save(String chatId, MultipartFile file) {
        try {
            // 1. 上传文件到OSS
            String fileUrl = ossUtil.uploadFile(file, chatId);
            
            // 2. 保存chatId和文件URL到数据库
            chatDocumentService.saveDocument(chatId, fileUrl);

            // 3. 向Redis向量数据库中添加文件
            vectorStoreUtils.redisVectorStore(fileUrl);

            log.info("文件上传成功，chatId: {}, fileUrl: {}", chatId, fileUrl);
            return true;
            
        } catch (Exception e) {
            log.error("文件上传失败，chatId: {}", chatId, e);
            return false;
        }
    }

    /**
     * 根据chatId获取文件URL
     * @param chatId 会话id
     * @return
     */
    @Override
    public String getFile(String chatId) {
        // 从数据库中查询文件URL
        ChatDocument documents = chatDocumentService.getDocumentsByChatId(chatId);
        if (documents == null) {
            log.warn("未找到与chatId相关的文件记录，chatId: {}", chatId);
            return null;
        }
        // 假设每个chatId只有一个文件记录，返回第一个
        return documents.getFileUrl();
    }
}
