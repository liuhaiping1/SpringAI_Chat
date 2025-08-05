package com.example.springai_chat.mapper;

import com.example.springai_chat.entity.po.Course;
import com.example.springai_chat.entity.query.CourseQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import java.util.List;

@Mapper
public interface CourseMapper {

    /**
     * 查询所有课程
     */
    List<Course> selectAll();

    /**
     * 动态查询课程
     * 支持按类型、教育等级查询，支持排序
     */
    List<Course> selectCourses(CourseQuery query);

    /**
     * 按类型查询
     */
    List<Course> selectByType(String type);

    /**
     * 按教育等级查询
     */
    List<Course> selectByEdu(Integer edu);
}
