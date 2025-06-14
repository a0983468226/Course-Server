package com.course.mapper.vo;

import lombok.Data;

import java.util.Date;

@Data
public class EnrollmentVO {

    private String id;
    private String studentId;
    private String courseId;
    private String status;
    private Date enrolledAt;

}
