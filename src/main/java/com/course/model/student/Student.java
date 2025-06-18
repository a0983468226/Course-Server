package com.course.model.student;

import lombok.Data;

import java.util.Date;

@Data
public class Student {

    private String id;
    private String username;
    private String name;
    private String email;
    private Date createdAt;
    private int status;
}
