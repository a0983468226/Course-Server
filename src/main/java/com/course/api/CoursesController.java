package com.course.api;

import com.course.enums.CourseStatus;
import com.course.enums.UserRole;
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

    // 查詢課程
    @GetMapping()
    public BasicResponse<FindCoursesByIdResponse> findCourses() {
        return ResponseUtil.execute(
                () -> {
                    List<CourseDetailVO> vos = coursesService.findCoursesDetail();
                    List<Course> cList = new ArrayList<>();
                    for (CourseDetailVO vo : vos) {
                        Course c = new Course();
                        BeanUtils.copyProperties(c, vo);
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

    // 查詢我的課程
    @GetMapping("/my")
    public BasicResponse<FindCoursesByIdResponse> findTeacherCoursesById(@AuthenticationPrincipal JwtUserDetails user) {
        return ResponseUtil.execute(
                () -> {
                    String role = user.getRoles().get(0);
                    List<CourseDetailVO> vos;
                    if (role.equals(UserRole.ADMIN.toString())) {
                        vos = coursesService.findPaddingCoursesDetail();
                    } else if (role.equals(UserRole.TEACHER.toString())) {
                        vos = coursesService.findTeacherCoursesDetail(user.getUserId());
                    } else {
                        vos = coursesService.findCoursesDetailByUserId(user.getUserId());
                    }

                    List<Course> cList = new ArrayList<>();
                    for (CourseDetailVO vo : vos) {
                        Course c = new Course();
                        BeanUtils.copyProperties(c, vo);
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

    // 查詢單一課程
    @GetMapping("/{id}")
    public BasicResponse<CoursesResponse> findCoursesById(@PathVariable String id) {
        return ResponseUtil.execute(
                () -> {
                    CoursesResponse data = new CoursesResponse();
                    CourseDetailVO vo = coursesService.findById(id);
                    if (vo != null) {
                        BeanUtils.copyProperties(data, vo);
                    }
                    return data;
                },
                "success",
                "更新課程失敗"
        );
    }

    // 查詢單一課程加簽/退選申請
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
                        BeanUtils.copyProperties(detail, vo);
                        list.add(detail);
                    }
                    data.setCoursesRequestsDetails(list);
                    return data;
                },
                "success",
                "查詢失敗"
        );
    }

    // 查詢單一學期所有課程
    @GetMapping("/{id}/semesters")
    public BasicResponse<FindSemestersByCourseResponse> findSemestersByCourse(@PathVariable String id) {
        return ResponseUtil.execute(
                () -> {
                    FindSemestersByCourseResponse data = new FindSemestersByCourseResponse();
                    List<Semester> list = new ArrayList<>();
                    List<CourseDetailVO> vos = coursesService.findCoursesDetailBySemesters(id);
                    for (CourseDetailVO vo : vos) {
                        Semester detail = new Semester();
                        BeanUtils.copyProperties(detail, vo);
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
    @AdminOnly
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

    // 更新課程資料
    @PutMapping()
    @TeacherOnly
    public BasicResponse<InsertCoursesResponse> updateCourses(@AuthenticationPrincipal JwtUserDetails user,
                                                              @RequestBody CourseQueryParam query) {
        return ResponseUtil.execute(
                () -> {
                    CourseVO vo = new CourseVO();
                    BeanUtils.copyProperties(vo, query);
                    coursesService.update(vo, user.getUserId());
                    return new InsertCoursesResponse();
                },
                "success",
                "更新課程失敗"
        );
    }

    // 新增課程
    @PostMapping()
    @TeacherOnly
    public BasicResponse<InsertCoursesResponse> insertCourses(@AuthenticationPrincipal JwtUserDetails user,
                                                              @RequestBody CourseQueryParam query) {
        return ResponseUtil.execute(
                () -> {
                    CourseVO vo = new CourseVO();
                    BeanUtils.copyProperties(vo, query);
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

    // 刪除課程
    @DeleteMapping("/{id}")
    @TeacherOnly
    public BasicResponse<DeleteCoursesResponse> deleteCourses(@AuthenticationPrincipal JwtUserDetails user,@PathVariable String id) {
        return ResponseUtil.execute(
                () -> {
                    coursesService.delete(id , user.getUserId());
                    return new DeleteCoursesResponse();
                },
                "success",
                "刪除課程失敗"
        );
    }
}
