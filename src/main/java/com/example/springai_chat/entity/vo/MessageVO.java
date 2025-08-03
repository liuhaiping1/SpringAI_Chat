package com.example.springai_chat.entity.vo;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.chat.messages.Message;

/**
 * 消息视图对象
 *
 * 用于前端展示的消息数据传输对象，将Spring AI的Message对象
 * 转换为更简洁的格式，包含角色标识和消息内容。
 * 通过Lombok的@Data注解自动生成getter、setter、toString等方法。
 */
@Data
@NoArgsConstructor
public class MessageVO {
    /**
     * 消息角色标识
     *
     * 标识消息的发送者类型，可能的值包括：
     * - user: 用户发送的消息
     * - assistant: AI助手回复的消息
     * - system: 系统消息
     * - tool: 工具调用消息
     */
    private String role;

    /**
     * 消息内容
     *
     * 消息的文本内容，直接对应原始Message对象中的文本信息
     */
    private String content;

    /**
     * 从Message对象构造MessageVO
     *
     * 将Spring AI的Message对象转换为前端友好的MessageVO格式。
     * 根据消息类型映射为对应的角色字符串，便于前端识别和处理。
     *
     * @param message Spring AI的原始消息对象，包含消息类型和文本内容
     */
    public MessageVO(Message  message) {
        // 根据消息类型映射为对应的角色字符串
        switch (message.getMessageType()) {
            case USER:
                role = "user";
                break;
            case ASSISTANT:
                role = "assistant";
                break;
            case SYSTEM:
                role = "system";
                break;
            case TOOL:
                role = "tool";
                break;
            default:
                // 未知类型设置为空格，避免null值导致的前端处理问题
                role = " ";
                break;
        }
        // 提取消息文本内容
        this.content = message.getText();
    }
}