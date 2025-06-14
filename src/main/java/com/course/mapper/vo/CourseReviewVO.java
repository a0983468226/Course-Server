package com.course.mapper.vo;

import lombok.Data;

import java.util.Date;
@Data
public class CourseReviewVO {
    private String id;
    private String courseId;
    private String studentId;
    private String rating;
    private String comment;
    private Date createdAt;
}
