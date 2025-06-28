package com.course.model.user;

import lombok.Data;

@Data
public class UpdateUserRequest {

    private String id;
    private String username;
    private String password;
    private String name;
    private String email;
    private String isFirstLogin;
    private String status;
    private String role;
}
