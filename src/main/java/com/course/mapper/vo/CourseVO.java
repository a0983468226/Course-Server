package com.course.mapper.vo;

import lombok.Data;

@Data
public class CourseVO {

    private String id;
    private String code;
    private String name;
    private String description;
    private Integer credit;
    private String teacherId;
    private Integer capacity;
    private String semesterId;
    private String schedule;
    private String location;
    private String status;
    private Integer studyNumber;
}
