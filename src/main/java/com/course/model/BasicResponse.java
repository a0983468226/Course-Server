package com.course.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class BasicResponse<T extends AbstractResponseData> implements Serializable {

    private Boolean success = false;

    private int code;

    private String errorType;

    private String message;

    private T data;

}
