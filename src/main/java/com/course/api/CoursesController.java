package com.course.api;

import com.course.mapper.vo.CourseVO;
import com.course.model.BasicResponse;
import com.course.model.courses.*;
import com.course.service.CoursesService;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/courses")
public class CoursesController {

    @Autowired
    private CoursesService coursesService;

    @GetMapping()
    public BasicResponse<CoursesListResponse> findCoursesById(CourseQueryParam query) {
        BasicResponse<CoursesListResponse> response = new BasicResponse<CoursesListResponse>();
        CoursesListResponse data = new CoursesListResponse();
        try {
            List<CourseVO> vos = coursesService.findByParam(query);
            List<Courses> list = new ArrayList<>();
            for (CourseVO vo : vos) {
                Courses Course = new Courses();
                BeanUtils.copyProperties(vo, Course);
                list.add(Course);
            }
            data.setCourses(list);
            response.setData(data);
            response.setMessage("success");
            response.setSuccess(true);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.setMessage("課程搜尋失敗");
            response.setSuccess(false);
            return response;
        }
    }

    @PostMapping()
    public BasicResponse<InsertCoursesResponse> insertCourses(@RequestBody CourseQueryParam query) {
        BasicResponse<InsertCoursesResponse> response = new BasicResponse<>();
        InsertCoursesResponse data = new InsertCoursesResponse();
        try {

            response.setData(data);
            response.setMessage("success");
            response.setSuccess(true);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.setMessage("新增課程失敗");
            response.setSuccess(false);
            return response;
        }
    }

    @GetMapping("/:id")
    public BasicResponse<CoursesResponse> findCoursesById(@PathVariable String id) {
        BasicResponse<CoursesResponse> response = new BasicResponse<CoursesResponse>();
        CoursesResponse data = new CoursesResponse();
        try {
            CourseVO vo = coursesService.findById(id);
            if (vo != null) {
                BeanUtils.copyProperties(vo, data);
            }
            response.setData(data);
            response.setMessage("success");
            response.setSuccess(true);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.setMessage("課程查詢失敗");
            response.setSuccess(false);
            return response;
        }
    }


}
