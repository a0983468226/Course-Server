package com.course.service;

import com.course.enums.CourseRequestsStatus;
import com.course.enums.CourseRequestsType;
import com.course.enums.EnrollmentsStatus;
import com.course.mapper.CourseRequestsMapper;
import com.course.mapper.CoursesMapper;
import com.course.mapper.EnrollmentsMapper;
import com.course.mapper.vo.CourseDetailVO;
import com.course.mapper.vo.CourseRequestVO;
import com.course.mapper.vo.EnrollmentVO;
import com.course.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class CourseRequestsService {

    @Autowired
    private CoursesMapper coursesMapper;

    @Autowired
    private CourseRequestsMapper courseRequestsMapper;

    @Autowired
    private EnrollmentsMapper enrollmentsMapper;

    public void insertCourseRequests(String userId, String courseId, String reason) throws Exception {
        CourseRequestVO vo = new CourseRequestVO();
        vo.setId(CommonUtil.getUUID());
        vo.setCourseId(courseId);
        vo.setStudentId(userId);
        vo.setReason(reason);
        vo.setType(CourseRequestsType.ADD.name());
        vo.setStatus(CourseRequestsStatus.PENDING.name());
        vo.setCreatedAt(new Date());
        courseRequestsMapper.insert(vo);
    }

    public void updateCourseRequests(String id, String reason) throws Exception {
        CourseRequestVO vo = new CourseRequestVO();
        vo.setId(id);
        vo.setReason(reason);
        vo.setType(CourseRequestsType.DROP.name());
        courseRequestsMapper.update(vo);
    }

    @Transactional
    public void updateCourseRequestsStatus(String id, String status) throws Exception {
        courseRequestsMapper.updateStatusById(status, id);

        if (CourseRequestsStatus.APPROVED.name().equals(status)) {
            CourseRequestVO vo = courseRequestsMapper.findById(id);
            EnrollmentVO enrollmentVO = new EnrollmentVO();
            enrollmentVO.setId(CommonUtil.getUUID());
            enrollmentVO.setStudentId(vo.getStudentId());
            enrollmentVO.setCourseId(vo.getCourseId());
            enrollmentVO.setEnrolledAt(new Date());
            enrollmentVO.setStatus(EnrollmentsStatus.ENROLLED.name());
            enrollmentsMapper.insert(enrollmentVO);

            CourseDetailVO courseDetailVO = coursesMapper.findById(vo.getCourseId());
            int i = courseDetailVO.getStudyNumber() + 1;
            coursesMapper.updateStudyNumber(courseDetailVO.getId(), i);
        } else if (CourseRequestsStatus.REJECTED.name().equals(status)) {
            CourseRequestVO vo = courseRequestsMapper.findById(id);
            List<EnrollmentVO> list = enrollmentsMapper.findByStudentAndCourse(vo.getStudentId(), vo.getCourseId());
            if (list.size() != 0) {
                EnrollmentVO enrollmentVO = list.getFirst();
                enrollmentsMapper.updateByStatus(enrollmentVO.getId(), EnrollmentsStatus.WITHDRAWN.name());
            }

            CourseDetailVO courseDetailVO = coursesMapper.findById(vo.getCourseId());
            int i = courseDetailVO.getStudyNumber() - 1;
            coursesMapper.updateStudyNumber(courseDetailVO.getId(), i);
        }
    }
}
