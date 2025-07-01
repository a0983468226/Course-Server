package com.course.model.student;

import com.course.model.AbstractResponseData;
import lombok.Data;

import java.util.List;

@Data
public class EnrollmentResponse extends AbstractResponseData {
    private List<Enrollment> enrollments;

}
