package com.course.model.user;

import lombok.Data;

@Data
public class InsertUserRequest {

    private String username;

    private String password;

    private String name;

    private String email;

    private String role;

}
