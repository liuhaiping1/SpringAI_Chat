package com.example.springai_chat.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Component
@Slf4j
public class OssUtil {
    
    @Value("${aliyun.oss.endpoint}")
    private String endpoint;
    
    @Value("${aliyun.oss.access-key-id}")
    private String accessKeyId;
    
    @Value("${aliyun.oss.access-key-secret}")
    private String accessKeySecret;
    
    @Value("${aliyun.oss.bucket-name}")
    private String bucketName;
    
    /**
     * 获取OSS客户端
     */
    private OSS getOssClient() {
        return new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }
    
    /**
     * 上传文件到OSS
     * @param file 要上传的文件
     * @param chatId 聊天ID
     * @return 文件URL
     */
    public String uploadFile(MultipartFile file, String chatId) {
        OSS ossClient = null;
        try {
            ossClient = getOssClient();
            
            String fileName = file.getOriginalFilename();
            String fileExtension = fileName.substring(fileName.lastIndexOf("."));
            String objectKey = String.format("rag/%s/%s%s", chatId, UUID.randomUUID().toString(), fileExtension);
            
            try (InputStream inputStream = file.getInputStream()) {
                PutObjectRequest putObjectRequest = new PutObjectRequest(
                    bucketName, objectKey, inputStream);
                ossClient.putObject(putObjectRequest);
                
                // 返回文件URL
                return String.format("https://%s.%s/%s", 
                    bucketName, 
                    endpoint.replace("https://", ""), 
                    objectKey);
            }
        } catch (IOException e) {
            log.error("文件上传失败", e);
            throw new RuntimeException("文件上传失败: " + e.getMessage(), e);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
    
    /**
     * 删除OSS文件
     * @param objectKey 文件key
     * @return 是否删除成功
     */
    public boolean deleteFile(String objectKey) {
        OSS ossClient = null;
        try {
            ossClient = getOssClient();
            ossClient.deleteObject(bucketName, objectKey);
            log.info("文件删除成功: {}", objectKey);
            return true;
        } catch (Exception e) {
            log.error("文件删除失败: {}", objectKey, e);
            return false;
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }


    /**
     * 根据OSS URL删除文件
     * @param ossUrl OSS文件的完整URL
     * @return 是否删除成功
     */
    public boolean deleteFileByUrl(String ossUrl) {
        if (ossUrl == null || ossUrl.trim().isEmpty()) {
            log.error("OSS URL为空");
            return false;
        }

        String objectKey = extractObjectKeyFromUrl(ossUrl);
        if (objectKey == null) {
            log.error("无法从URL中提取objectKey: {}", ossUrl);
            return false;
        }

        return deleteFile(objectKey);
    }

    /**
     * 从OSS URL中提取objectKey
     * @param ossUrl OSS文件的完整URL
     * @return 提取出的objectKey，如果提取失败返回null
     */
    private String extractObjectKeyFromUrl(String ossUrl) {
        try {
            // 去掉协议部分 https:// 或 http://
            String urlWithoutProtocol = ossUrl.replaceFirst("^https?://", "");

            // 找到第一个斜杠的位置
            int firstSlashIndex = urlWithoutProtocol.indexOf("/");

            if (firstSlashIndex != -1 && firstSlashIndex < urlWithoutProtocol.length() - 1) {
                // 提取域名后的路径部分
                return urlWithoutProtocol.substring(firstSlashIndex + 1);
            }

            return null;
        } catch (Exception e) {
            log.error("解析OSS URL失败: {}", ossUrl, e);
            return null;
        }
    }





    
    /**
     * 检查文件是否存在
     * @param objectKey 文件key
     * @return 是否存在
     */
    public boolean fileExists(String objectKey) {
        OSS ossClient = null;
        try {
            ossClient = getOssClient();
            return ossClient.doesObjectExist(bucketName, objectKey);
        } catch (Exception e) {
            log.error("检查文件是否存在失败: {}", objectKey, e);
            return false;
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
}