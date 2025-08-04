package com.example.springai_chat.entity.query;

import lombok.Data;
import org.springframework.ai.tool.annotation.ToolParam;

import java.util.List;

@Data
public class CourseQuery {
    @ToolParam(required = false, description = "课程类型：编程、设计、⾃媒体、其它")
    private String type;

    @ToolParam(required = false, description = "学历要求：0-⽆、1-初中、2-⾼中、3-⼤专、4-本科及本科以上")
    private Integer edu;

    @ToolParam(required = false, description = "排序偏好：根据价格敏感或时长敏感进行排序")
    private List<Sort> sorts;

    @Data
    public static class Sort {
        @ToolParam(required = false, description = "排序字段: price(价格敏感) 或 duration(时长敏感)")
        private String field;
        @ToolParam(required = false, description = "是否升序排列: true-价格从低到高/时长从短到长，false-降序")
        private Boolean asc;
    }
}
