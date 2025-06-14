package com.course.mapper.vo;

import lombok.Data;

import java.util.Date;

@Data
public class UserVO {

    private String id;
    private String username;
    private String passwordHash;
    private String name;
    private String email;
    private String role;
    private Date createdAt;
    private int status;

}
