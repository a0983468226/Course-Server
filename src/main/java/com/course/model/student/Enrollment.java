package com.course.model.student;

import lombok.Data;

import java.util.Date;

@Data
public class Enrollment {
    private String id;
    private String studentId;
    private String courseId;
    private String status;
    private String statusDesc;
    private Date enrolledAt;
}
