package com.example.springai_chat.tools;


import com.example.springai_chat.entity.po.Course;
import com.example.springai_chat.entity.po.CourseReservation;
import com.example.springai_chat.entity.po.School;
import com.example.springai_chat.entity.query.CourseQuery;
import com.example.springai_chat.service.ICourseReservationService;
import com.example.springai_chat.service.ICourseService;
import com.example.springai_chat.service.ISchoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class CourseTools {

    private final ICourseService courseService;
    private final ISchoolService schoolService;
    private final ICourseReservationService courseReservationService;

    @Tool(description = "根据条件查询课程信息")
    public List<Course> queryCourses(@ToolParam(description = "课程查询条件",required = false) CourseQuery query) {
        if (query == null) {
            return courseService.list();
        }
        return courseService.queryByCondition(query);
    }

    @Tool(description = "查询所有校区信息")
    public List<School> querySchool() {
        return schoolService.list();
    }

    @Tool(description = "创建课程预约,返回预约单号")
    public Integer createCourseReservation(@ToolParam(description = "预约课程") String course,
                                          @ToolParam(description = "预约校区") String school,
                                           @ToolParam(description = "预约人姓名") String studentName,
                                           @ToolParam(description = "预约人联系方式") String contactInfo,
                                           @ToolParam(description = "预约备注",required = false) String remark) {
        CourseReservation courseReservation = new CourseReservation();
        courseReservation.setCourse(course);
        courseReservation.setSchool(school);
        courseReservation.setStudentName(studentName);
        courseReservation.setContactInfo(contactInfo);
        courseReservation.setRemark(remark);
        courseReservationService.save(courseReservation);

        return courseReservation.getId();
    }
}
