package com.example.springai_chat.mapper;

import com.example.springai_chat.entity.po.CourseReservation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface CourseReservationMapper {

    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(CourseReservation courseReservation);
}
