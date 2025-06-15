package com.course.model.user;

import lombok.Data;
import lombok.NonNull;

import java.util.Date;

@Data
public class UpdateUserRequest {

    @NonNull
    private String id;
    private String password;
    private String name;
    private String email;
    private String isFirstLogin;
    private String status;
    private String role;
}
