package com.course.service;

import com.course.mapper.CoursesMapper;
import com.course.mapper.vo.CourseVO;
import com.course.model.courses.CourseQueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoursesService {

    @Autowired
    private CoursesMapper coursesMapper;

    public List<CourseVO> findByParam(CourseQueryParam request) throws Exception {
        return coursesMapper.findByParam(request);
    }

    public CourseVO findById(String id) throws Exception {
        return coursesMapper.findById(id);
    }

}
