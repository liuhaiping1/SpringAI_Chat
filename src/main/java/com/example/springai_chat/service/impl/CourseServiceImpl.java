package com.example.springai_chat.service.impl;

import com.example.springai_chat.entity.po.Course;
import com.example.springai_chat.entity.query.CourseQuery;
import com.example.springai_chat.mapper.CourseMapper;
import com.example.springai_chat.service.ICourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author author
 * @since 2025-08-04
 */
@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements ICourseService {
    
    private final CourseMapper courseMapper;
    
    @Override
    public List<Course> list() {
        return courseMapper.selectAll();
    }
    
    @Override
    public List<Course> queryByCondition(CourseQuery query) {
        if (query == null) {
            return courseMapper.selectAll();
        }
        
        String type = query.getType();
        Integer edu = query.getEdu();
        
        // 处理排序
        String orderField = "id";
        String orderDirection = "ASC";
        if (query.getSorts() != null && !query.getSorts().isEmpty()) {
            CourseQuery.Sort sort = query.getSorts().get(0);
            orderField = sort.getField();
            orderDirection = sort.getAsc() ? "ASC" : "DESC";
        }
        
        return courseMapper.selectCourses(query);
    }
}
