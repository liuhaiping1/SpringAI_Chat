package com.example.springai_chat.service.impl;

import com.example.springai_chat.entity.po.School;
import com.example.springai_chat.mapper.SchoolMapper;
import com.example.springai_chat.service.ISchoolService;
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
public class SchoolServiceImpl implements ISchoolService {
    
    private final SchoolMapper schoolMapper;
    
    @Override
    public List<School> list() {
        return schoolMapper.selectAll();
    }
}
