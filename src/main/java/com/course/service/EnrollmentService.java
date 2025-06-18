package com.course.service;

import com.course.mapper.EnrollmentsMapper;
import com.course.mapper.vo.EnrollmentStudentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnrollmentService {
    @Autowired
    private EnrollmentsMapper enrollmentsMapper;

    public List<EnrollmentStudentVO>  findByCoursesId(String id) throws Exception {
        return enrollmentsMapper.findByCoursesId(id);
    }
}
