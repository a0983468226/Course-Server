package com.course.api;

import com.course.mapper.vo.EnrollmentStudentVO;
import com.course.model.BasicResponse;
import com.course.model.teacher.EnrollmentStudent;
import com.course.model.teacher.FindStudentByCourseResponse;
import com.course.service.CoursesService;
import com.course.service.EnrollmentService;
import com.course.util.ResponseUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/teachers")
public class TeacherController {

    @Autowired
    private CoursesService coursesService;

    @Autowired
    private EnrollmentService enrollmentService;

    @GetMapping("/courses/{id}/students")
    public BasicResponse<FindStudentByCourseResponse> FindStudentByCourses(@PathVariable String id) {
        return ResponseUtil.execute(
                () -> {
                    List<EnrollmentStudentVO> vos = enrollmentService.findByCoursesId(id);
                    List<EnrollmentStudent> eList = new ArrayList<>();
                    for (EnrollmentStudentVO vo : vos) {
                        EnrollmentStudent c = new EnrollmentStudent();
                        BeanUtils.copyProperties(vo, c);
                        eList.add(c);
                    }
                    FindStudentByCourseResponse data = new FindStudentByCourseResponse();
                    data.setStudents(eList);
                    return data;
                },
                "success",
                "查詢失敗"
        );
    }



}
