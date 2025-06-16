package com.course.model.courses;

import com.course.model.AbstractResponseData;
import lombok.Data;

import java.util.List;

@Data
public class CoursesListResponse extends AbstractResponseData {

    private List<Course> courses;
}
