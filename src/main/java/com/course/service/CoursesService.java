package com.course.service;

import com.course.mapper.CoursesMapper;
import com.course.mapper.vo.CourseDetailVO;
import com.course.mapper.vo.CourseVO;
import com.course.mapper.vo.courseRequestDetailVO;
import com.course.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CoursesService {

    @Autowired
    private CoursesMapper coursesMapper;

    public List<CourseDetailVO> findCoursesDetail() throws Exception {
        return coursesMapper.findCoursesDetail();
    }

    public List<CourseDetailVO> findPaddingCoursesDetail() throws Exception {
        return coursesMapper.findPaddingCoursesDetail();
    }

    public CourseDetailVO findById(String id) throws Exception {
        return coursesMapper.findById(id);
    }

    public List<CourseDetailVO> findCoursesDetailByUserId(String userid) throws Exception {
        return coursesMapper.findCoursesDetailByUserId(userid);
    }

    public List<CourseDetailVO> findCoursesDetailBySemesters(String semesterId) throws Exception {
        return coursesMapper.findCoursesDetailBySemesters(semesterId);
    }

    public List<courseRequestDetailVO> findCourseRequestByCourseId(String courseId) throws Exception {
        return coursesMapper.findCourseRequestByCourseId(courseId);
    }

    @Transactional
    public synchronized void insert(CourseVO vo) throws Exception {
        vo.setId(CommonUtil.getUUID());
        int count = coursesMapper.insert(vo);
        if (count != 1) {
            throw new IllegalStateException("新增資料不為1筆");
        }
    }

    @Transactional
    public synchronized void updateStatus(String id, String status) throws Exception {
        int count = coursesMapper.updateStatus(id, status);
        if (count != 1) {
            throw new IllegalStateException("更新資料不為1筆");
        }
    }

    @Transactional
    public synchronized void update(CourseVO vo, String userId) throws Exception {
        int count = coursesMapper.teacherUpdateCourse(vo, userId);
        if (count != 1) {
            throw new IllegalStateException("更新資料不為1筆");
        }
    }

    @Transactional
    public synchronized void delete(String id) throws Exception {
        int count = coursesMapper.delete(id);
        if (count != 1) {
            throw new IllegalStateException("刪除資料不為1筆");
        }
    }

}
