package com.course.model.semesters;

import lombok.Data;

@Data
public class InsertSemestersRequest {

    private String name;
    private String startAt;
    private String endAt;
}
