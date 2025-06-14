package com.course.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginRequest implements Serializable {
    private String username;
    private String password;
    private String captchaId;
    private String captchaCode;
}
