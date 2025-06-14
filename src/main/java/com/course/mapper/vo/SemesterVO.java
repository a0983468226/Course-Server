package com.course.mapper.vo;

import lombok.Data;

import java.util.Date;

@Data
public class SemesterVO {
    private String id;
    private String name;
    private Date startAt;
    private Date endAt;
}
