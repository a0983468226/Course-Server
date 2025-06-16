package com.course.model.user;

import com.course.model.AbstractResponseData;
import lombok.Data;

import java.util.Date;

@Data
public class FinduserByIdResponse extends AbstractResponseData {

    private String id;
    private String username;
    private String passwordHash;
    private String name;
    private String email;
    private String role;
    private Date createdAt;
    private int status;
    private String isFirstLogin;
    private Date updateAt;
    private Date lastLoginTime;
}
