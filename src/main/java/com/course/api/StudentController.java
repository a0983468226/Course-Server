package com.course.api;


import com.course.model.BasicResponse;
import com.course.model.CommonResponse;
import com.course.model.student.StudentCourseRequest;
import com.course.security.jwt.JwtUserDetails;
import com.course.service.CourseRequestsService;
import com.course.service.EnrollmentService;
import com.course.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private CourseRequestsService courseRequestsService;



    // 選課
    @PostMapping("/enrollments/{id}")
    public BasicResponse<CommonResponse> insertEnrollment(@AuthenticationPrincipal JwtUserDetails user, @PathVariable String id) {
        return ResponseUtil.execute(
                () -> {
                    enrollmentService.insertEnrollment(user.getUserId(), id);
                    return new CommonResponse();
                },
                "success",
                "選課失敗"
        );
    }

    // 加簽申請
    @PostMapping("/course-requests")
    public BasicResponse<CommonResponse> addCourseRequests(@AuthenticationPrincipal JwtUserDetails user,
                                                           @RequestBody StudentCourseRequest rq) {
        return ResponseUtil.execute(
                () -> {
                    courseRequestsService.insertCourseRequests(user.getUserId(), rq.getCourseId(), rq.getReason());
                    return new CommonResponse();
                },
                "success",
                "加簽失敗"
        );
    }

    // 退選申請
    @PutMapping("/course-requests")
    public BasicResponse<CommonResponse> dropCourseRequests(@AuthenticationPrincipal JwtUserDetails user,
                                                            @RequestBody StudentCourseRequest rq) {
        return ResponseUtil.execute(
                () -> {
                    courseRequestsService.updateCourseRequests(rq.getId(), rq.getReason());
                    return new CommonResponse();
                },
                "success",
                "退選失敗"
        );
    }
}
