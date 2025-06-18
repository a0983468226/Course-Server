package com.course.model.user;

import lombok.Data;

import java.util.Date;

@Data
public class User {
    private String id;
    private String username;
    private String name;
    private String email;
    private String role;
    private Date createdAt;
    private int status;
    private String isFirstLogin;
    private Date updateAt;
    private Date lastLoginTime;
}
