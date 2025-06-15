package com.course.model.auth;

import com.course.model.AbstractResponseData;
import lombok.Data;

@Data

public class CaptchaResponse extends AbstractResponseData {
    private String id ;
    private String image;
}
