package com.course.model.semesters;

import lombok.Data;

import java.util.Date;

@Data
public class UpdateSemestersRequest {
    private String id;
    private String name;
    private Date startAt;
    private Date endAt;
}
