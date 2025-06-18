package com.course.mapper.vo;

import lombok.Data;

import java.util.Date;

@Data
public class EnrollmentStudentVO {

    private String id;
    private String status;
    private Date enrolledAt;
    private String studentId;
    private String studentName;
    private String email;

}
