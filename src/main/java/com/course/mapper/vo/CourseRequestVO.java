package com.course.mapper.vo;

import lombok.Data;

import java.util.Date;

@Data
public class CourseRequestVO {

    private String id;
    private String studentId;
    private String courseId;
    private String type;
    private String reason;
    private String status;
    private Date createdAt;

}
