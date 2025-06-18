package com.course.model.teacher;

import lombok.Data;

import java.util.Date;

@Data
public class EnrollmentStudent {
    private String id;
    private String status;
    private Date enrolledAt;
    private String studentId;
    private String studentName;
    private String email;
}
