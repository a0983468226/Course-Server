package com.course.api;

import com.course.enums.CourseStatus;
import com.course.mapper.vo.CourseDetailVO;
import com.course.mapper.vo.CourseVO;
import com.course.mapper.vo.courseRequestDetailVO;
import com.course.model.BasicResponse;
import com.course.model.courses.*;
import com.course.model.semesters.Semester;
import com.course.model.teacher.FindCoursesByIdResponse;
import com.course.security.authorize.AdminOnly;
import com.course.security.authorize.TeacherOnly;
import com.course.security.jwt.JwtUserDetails;
import com.course.service.CoursesService;
import com.course.util.CommonUtil;
import com.course.util.ResponseUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/courses")
public class CoursesController {

    @Autowired
    private CoursesService coursesService;

    @GetMapping("/{id}")
    public BasicResponse<CoursesResponse> findCoursesById(@PathVariable String id) {
        return ResponseUtil.execute(
                () -> {
                    CoursesResponse data = new CoursesResponse();
                    CourseDetailVO vo = coursesService.findById(id);
                    if (vo != null) {
                        BeanUtils.copyProperties(vo, data);
                    }
                    return data;
                },
                "success",
                "更新課程失敗"
        );
    }

    @GetMapping("/{id}/course-requests")
    @TeacherOnly
    public BasicResponse<findCoursesRequestsByCourseResponse> findCoursesRequestsByCourse(@PathVariable String id) {
        return ResponseUtil.execute(
                () -> {
                    findCoursesRequestsByCourseResponse data = new findCoursesRequestsByCourseResponse();
                    List<CoursesRequestsDetail> list = new ArrayList<>();
                    List<courseRequestDetailVO> vos = coursesService.findCourseRequestByCourseId(id);
                    for (courseRequestDetailVO vo : vos) {
                        CoursesRequestsDetail detail = new CoursesRequestsDetail();
                        BeanUtils.copyProperties(vo, detail);
                        list.add(detail);
                    }
                    data.setCoursesRequestsDetails(list);
                    return data;
                },
                "success",
                "查詢失敗"
        );
    }

    @GetMapping("/{id}/semesters")
    public BasicResponse<FindSemestersByCourseResponse> findSemestersByCourse(@PathVariable String id) {
        return ResponseUtil.execute(
                () -> {
                    FindSemestersByCourseResponse data = new FindSemestersByCourseResponse();
                    List<Semester> list = new ArrayList<>();
                    List<CourseDetailVO> vos = coursesService.findCoursesDetailBySemesters(id);
                    for (CourseDetailVO vo : vos) {
                        Semester detail = new Semester();
                        BeanUtils.copyProperties(vo, detail);
                        list.add(detail);
                    }
                    data.setSemesters(list);
                    return data;
                },
                "success",
                "查詢失敗"
        );
    }

    // 管理者審核課程
    @PutMapping("/audit")
    @TeacherOnly
    public BasicResponse<InsertCoursesResponse> updateCoursesByAdmin(
            @RequestBody AuditRequest query) {
        return ResponseUtil.execute(
                () -> {
                    CourseVO vo = new CourseVO();
                    coursesService.updateStatus(query.getId(), query.getStatus());
                    return new InsertCoursesResponse();
                },
                "success",
                "更新課程失敗"
        );
    }

    @GetMapping("/teacher/{id}")
    public BasicResponse<FindCoursesByIdResponse> findTeacherCoursesById(@PathVariable String id) {
        return ResponseUtil.execute(
                () -> {
                    List<CourseDetailVO> vos = coursesService.findCoursesDetailByTeacher(id);
                    List<Course> cList = new ArrayList<>();
                    for (CourseDetailVO vo : vos) {
                        Course c = new Course();
                        BeanUtils.copyProperties(vo, c);
                        cList.add(c);
                    }
                    FindCoursesByIdResponse data = new FindCoursesByIdResponse();
                    data.setCourses(cList);
                    return data;
                },
                "success",
                "查詢失敗"
        );
    }

    @PutMapping()
    @TeacherOnly
    public BasicResponse<InsertCoursesResponse> updateCourses(@AuthenticationPrincipal JwtUserDetails user,
                                                              @RequestBody CourseQueryParam query) {
        return ResponseUtil.execute(
                () -> {
                    CourseVO vo = new CourseVO();
                    BeanUtils.copyProperties(query, vo);
                    coursesService.update(vo, user.getUserId());
                    return new InsertCoursesResponse();
                },
                "success",
                "更新課程失敗"
        );
    }

    @PostMapping()
    @TeacherOnly
    public BasicResponse<InsertCoursesResponse> insertCourses(@AuthenticationPrincipal JwtUserDetails user,
                                                              @RequestBody CourseQueryParam query) {
        return ResponseUtil.execute(
                () -> {
                    CourseVO vo = new CourseVO();
                    BeanUtils.copyProperties(query, vo);
                    vo.setId(CommonUtil.getUUID());
                    vo.setTeacherId(user.getUserId());
                    vo.setStatus(CourseStatus.PENDING.getValue());
                    coursesService.insert(vo);
                    return new InsertCoursesResponse();
                },
                "success",
                "新增課程失敗"
        );
    }

    @DeleteMapping("/{id}")
    @AdminOnly
    public BasicResponse<DeleteCoursesResponse> deleteCourses(@PathVariable String id) {
        return ResponseUtil.execute(
                () -> {
                    coursesService.delete(id);
                    return new DeleteCoursesResponse();
                },
                "success",
                "刪除課程失敗"
        );
    }
}
