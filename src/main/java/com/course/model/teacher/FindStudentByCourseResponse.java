package com.course.model.teacher;

import com.course.model.AbstractResponseData;
import lombok.Data;

import java.util.List;

@Data
public class FindStudentByCourseResponse extends AbstractResponseData {
    private List<EnrollmentStudent> students;
}
