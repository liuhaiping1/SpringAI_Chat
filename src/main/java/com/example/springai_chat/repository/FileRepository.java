package com.example.springai_chat.repository;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileRepository {
    /**
     * 保存文件,还要记录chatId与文件的映射关系
     * @param chatId 会话id
     * @param file 文件
     * @return 上传成功，返回true； 否则返回false
     */
    boolean save(String chatId, MultipartFile file);

    /**
     * 根据chatId获取文件URL
     * @param chatId 会话id
     * @return 找到的文件
     */
    String getFile(String chatId);
}
