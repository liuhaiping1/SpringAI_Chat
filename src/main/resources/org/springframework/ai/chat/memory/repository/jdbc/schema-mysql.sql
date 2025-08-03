CREATE TABLE IF NOT EXISTS SPRING_AI_CHAT_MEMORY (
                                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                                     conversation_id VARCHAR(255) NOT NULL,
                                                     content TEXT NOT NULL,
                                                     type VARCHAR(255) NOT NULL,
                                                     timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                                     INDEX idx_conversation_id (conversation_id),
                                                     INDEX idx_timestamp (timestamp)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;