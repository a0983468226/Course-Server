package com.course.mapper.vo;

import lombok.Data;

import java.util.Date;

@Data
public class CourseDetailVO extends CourseVO {

    private String teacherName;
    private String email;
    private String semestersName;
    private Date startAt;
    private Date endAt;

}
