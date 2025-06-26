package com.course.model.semesters;

import com.course.model.AbstractResponseData;
import lombok.Data;

import java.util.List;

@Data
public class SemestersResponse extends AbstractResponseData {
    private List<Semester> semesters;
}
