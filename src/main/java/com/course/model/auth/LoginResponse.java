package com.course.model.auth;

import com.course.model.AbstractResponseData;
import com.course.model.Menu;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class LoginResponse extends AbstractResponseData {
    private String accessToken;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date accessTokenExp;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date refreshTokenExp;

    private String userId;

    private String role;

    private String name;

    private String email;

    private List<Menu> menus;

    private String isFirstLogin;
}
