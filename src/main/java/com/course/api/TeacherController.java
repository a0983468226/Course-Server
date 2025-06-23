package com.course.api;

import com.course.mapper.vo.EnrollmentStudentVO;
import com.course.model.BasicResponse;
import com.course.model.CommonResponse;
import com.course.model.teacher.AddDropCourseRequest;
import com.course.model.teacher.EnrollmentStudent;
import com.course.model.teacher.FindStudentByCourseResponse;
import com.course.service.CourseRequestsService;
import com.course.service.EnrollmentService;
import com.course.util.ResponseUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/teachers")
public class TeacherController {

    @Autowired
    private CourseRequestsService courseRequestsService;

    @Autowired
    private EnrollmentService enrollmentService;

    //查詢單一課程所有學生
    @GetMapping("/courses/{id}/students")
    public BasicResponse<FindStudentByCourseResponse> findStudentByCourses(@PathVariable String id) {
        return ResponseUtil.execute(
                () -> {
                    List<EnrollmentStudentVO> vos = enrollmentService.findByCoursesId(id);
                    List<EnrollmentStudent> eList = new ArrayList<>();
                    for (EnrollmentStudentVO vo : vos) {
                        EnrollmentStudent c = new EnrollmentStudent();
                        BeanUtils.copyProperties(c, vo);
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

    // 學生加簽/退選申請
    @PostMapping("/courses/{id}/course-requests")
    public BasicResponse<CommonResponse> addDropCourse(@RequestBody AddDropCourseRequest request) {
        return ResponseUtil.execute(
                () -> {
                    courseRequestsService.updateCourseRequestsStatus(request.getId(), request.getStatus());
                    return new CommonResponse();
                },
                "success",
                "審核失敗"
        );
    }

}
