package com.course.model.courses;

import com.course.model.AbstractResponseData;
import com.course.model.semesters.Semester;
import lombok.Data;

import java.util.List;

@Data
public class FindSemestersByCourseResponse extends AbstractResponseData {
    private List<Semester> semesters;
}
