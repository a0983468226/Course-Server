package com.course.model.courses;

import lombok.Data;

import java.util.Date;

@Data
public class Course {
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
    private String teacherName;
    private String email;
    private String semestersName;
    private Date startAt;
    private Date endAt;
    private String studyNumber;
}
