package com.example.springai_chat.service;

import com.example.springai_chat.entity.po.CourseReservation;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author author
 * @since 2025-08-04
 */
public interface ICourseReservationService {
    
    boolean save(CourseReservation courseReservation);
}
