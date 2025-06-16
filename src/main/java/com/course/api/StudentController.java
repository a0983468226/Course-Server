package com.course.api;


import com.course.model.BasicResponse;
import com.course.model.student.FindEnrollmentsByIdRequest;
import com.course.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/{id}/enrollments")
    public BasicResponse<FindEnrollmentsByIdRequest> findEnrollmentsById(@PathVariable String id) {
        BasicResponse<FindEnrollmentsByIdRequest> response = new BasicResponse<FindEnrollmentsByIdRequest>();
        FindEnrollmentsByIdRequest data = new FindEnrollmentsByIdRequest();
        try {


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
