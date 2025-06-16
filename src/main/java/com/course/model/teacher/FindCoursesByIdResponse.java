package com.course.model.teacher;

import com.course.model.AbstractResponseData;
import com.course.model.courses.Course;
import lombok.Data;

import java.util.List;

@Data
public class FindCoursesByIdResponse extends AbstractResponseData {
    List<Course>  courses;
}
