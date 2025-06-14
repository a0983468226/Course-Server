package com.course.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class BasicResponse<T extends AbstractResponseData>  implements Serializable {

    private Boolean success = false;

    private String code;

    private String message;

    private T data;

}
