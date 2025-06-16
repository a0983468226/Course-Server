package com.course.api;

import com.course.mapper.vo.CourseDetailVO;
import com.course.model.BasicResponse;
import com.course.model.courses.Course;
import com.course.model.teacher.FindCoursesByIdResponse;
import com.course.service.CoursesService;
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

    @GetMapping("/{id}/courses")
    public BasicResponse<FindCoursesByIdResponse> findCoursesById(@PathVariable String id) {
        BasicResponse<FindCoursesByIdResponse> response = new BasicResponse<>();
        FindCoursesByIdResponse data = new FindCoursesByIdResponse();
        try {
            List<CourseDetailVO> vos = coursesService.findCoursesDetailByTeacher(id);
            List<Course> cList = new ArrayList<>();
            for (CourseDetailVO vo : vos) {
                Course c = new Course();
                BeanUtils.copyProperties(vo, c);
                cList.add(c);
            }
            data.setCourses(cList);

            response.setData(data);
            response.setMessage("success");
            response.setSuccess(true);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.setMessage("查詢失敗");
            response.setSuccess(false);
            return response;
        }
    }
}
