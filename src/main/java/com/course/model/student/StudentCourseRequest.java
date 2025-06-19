package com.course.model.student;

import lombok.Data;

@Data
public class StudentCourseRequest {
    private String id;
    private String courseId;
    private String reason;
}
