package com.course.service;

import com.course.enums.EnrollmentsStatus;
import com.course.exception.ServiceException;
import com.course.mapper.CoursesMapper;
import com.course.mapper.EnrollmentsMapper;
import com.course.mapper.vo.CourseDetailVO;
import com.course.mapper.vo.EnrollmentStudentVO;
import com.course.mapper.vo.EnrollmentVO;
import com.course.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class EnrollmentService {
    @Autowired
    private EnrollmentsMapper enrollmentsMapper;

    @Autowired
    private CoursesMapper coursesMapper;

    public List<EnrollmentStudentVO> findByCoursesId(String id) throws Exception {
        return enrollmentsMapper.findByCoursesId(id);
    }

    public List<EnrollmentVO> findByStudentId(String id) throws Exception {
        return enrollmentsMapper.findByStudent(id);
    }

    @Transactional
    public synchronized void insertEnrollment(String userId, String courseId) throws Exception {

        CourseDetailVO vo = coursesMapper.findById(courseId);

        if (vo == null) {
            throw new ServiceException("查無課程");
        }
        if (vo.getStudyNumber() >= vo.getCapacity()) {
            throw new ServiceException("人數超過課程上限");
        }
        EnrollmentVO enrollmentVO = new EnrollmentVO();
        enrollmentVO.setId(CommonUtil.getUUID());
        enrollmentVO.setStudentId(userId);
        enrollmentVO.setCourseId(courseId);
        enrollmentVO.setStatus(EnrollmentsStatus.ENROLLED.name());
        enrollmentVO.setEnrolledAt(new Date());
        enrollmentsMapper.insert(enrollmentVO);
    }
}
