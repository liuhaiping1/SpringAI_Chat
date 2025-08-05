package com.example.springai_chat.service.impl;

import com.example.springai_chat.entity.po.CourseReservation;
import com.example.springai_chat.mapper.CourseReservationMapper;
import com.example.springai_chat.service.ICourseReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
public class CourseReservationServiceImpl implements ICourseReservationService {
    
    private final CourseReservationMapper courseReservationMapper;
    
    @Override
    public boolean save(CourseReservation courseReservation) {
        return courseReservationMapper.insert(courseReservation) > 0;
    }
}
