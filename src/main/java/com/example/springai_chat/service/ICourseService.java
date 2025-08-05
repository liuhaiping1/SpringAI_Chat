package com.example.springai_chat.service;

import com.example.springai_chat.entity.po.Course;
import com.example.springai_chat.entity.query.CourseQuery;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author author
 * @since 2025-08-04
 */
public interface ICourseService {
    
    List<Course> list();
    
    List<Course> queryByCondition(CourseQuery query);
}
