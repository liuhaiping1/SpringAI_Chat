package com.example.springai_chat.entity.po;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author author
 * @since 2025-08-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CourseReservation implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String course;

    private String studentName;

    private String contactInfo;

    private String school;

    private String remark;

}
