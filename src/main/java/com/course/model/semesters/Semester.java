package com.course.model.semesters;

import lombok.Data;

@Data
public class Semester {
    private String id;
    private String name;
    private String startAt;
    private String endAt;
}
