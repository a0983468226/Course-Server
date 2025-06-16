package com.course.api;

import com.course.mapper.vo.CourseDetailVO;
import com.course.mapper.vo.CourseVO;
import com.course.model.BasicResponse;
import com.course.model.courses.CourseQueryParam;
import com.course.model.courses.CoursesResponse;
import com.course.model.courses.DeleteCoursesResponse;
import com.course.model.courses.InsertCoursesResponse;
import com.course.security.authorize.AdminOnly;
import com.course.security.authorize.TeacherOnly;
import com.course.security.jwt.JwtUserDetails;
import com.course.service.CoursesService;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/courses")
public class CoursesController {

    @Autowired
    private CoursesService coursesService;

    @GetMapping("/{id}")
    public BasicResponse<CoursesResponse> findCoursesById(@PathVariable String id) {
        BasicResponse<CoursesResponse> response = new BasicResponse<CoursesResponse>();
        CoursesResponse data = new CoursesResponse();
        try {
            CourseDetailVO vo = coursesService.findById(id);
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

    @PutMapping()
    @TeacherOnly
    public BasicResponse<InsertCoursesResponse> updateCourses(@AuthenticationPrincipal JwtUserDetails user, @RequestBody CourseQueryParam query) {
        BasicResponse<InsertCoursesResponse> response = new BasicResponse<>();
        InsertCoursesResponse data = new InsertCoursesResponse();
        try {
            CourseVO vo = new CourseVO();
            BeanUtils.copyProperties(query, vo);
            coursesService.update(vo, user.getUserId());

            response.setData(data);
            response.setMessage("success");
            response.setSuccess(true);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.setMessage("更新課程失敗");
            response.setSuccess(false);
            return response;
        }
    }

    @PostMapping()
    @AdminOnly
    public BasicResponse<InsertCoursesResponse> insertCourses(@RequestBody CourseQueryParam query) {
        BasicResponse<InsertCoursesResponse> response = new BasicResponse<>();
        InsertCoursesResponse data = new InsertCoursesResponse();
        try {
            CourseVO vo = new CourseVO();
            BeanUtils.copyProperties(query, vo);
            coursesService.insert(vo);

            response.setData(data);
            response.setMessage("success");
            response.setSuccess(true);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.setMessage("更新課程失敗");
            response.setSuccess(false);
            return response;
        }
    }

    @DeleteMapping("/{id}")
    @AdminOnly
    public BasicResponse<DeleteCoursesResponse> deleteCourses(@PathVariable String id) {
        BasicResponse<DeleteCoursesResponse> response = new BasicResponse<>();
        DeleteCoursesResponse data = new DeleteCoursesResponse();
        try {
            coursesService.delete(id);
            response.setData(data);
            response.setMessage("success");
            response.setSuccess(true);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.setMessage("刪除課程失敗");
            response.setSuccess(false);
            return response;
        }
    }
}
