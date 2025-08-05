package com.example.springai_chat.mapper;

import com.example.springai_chat.entity.po.School;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface SchoolMapper {
    
    @Select("SELECT * FROM school")
    List<School> selectAll();
}
