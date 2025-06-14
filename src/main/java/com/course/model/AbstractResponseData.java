package com.course.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public abstract class AbstractResponseData implements Serializable {

    private Date responseTime = new Date();
}
