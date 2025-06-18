package com.course.model.courses;

import lombok.Data;

import java.util.Date;

@Data
public class CoursesRequestsDetail {
    private String name;
    private String id;
    private String studentId;
    private String courseId;
    private String type;
    private String reason;
    private String status;
    private Date createdAt;
}
