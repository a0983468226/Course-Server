package com.course.mapper;

import com.course.mapper.sqlprovider.CourseSqlProvider;
import com.course.mapper.vo.CourseDetailVO;
import com.course.mapper.vo.CourseVO;
import com.course.mapper.vo.courseRequestDetailVO;
import com.course.model.courses.CourseQueryParam;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CoursesMapper {

    @Select("SELECT c.*,u.name as teacher_name,u.email, s.name as semesters_name,s.start_at , s.end_at FROM " +
            "courses c join users u on c.teacher_id = u.id " +
            "join semesters s on s.id = c.semester_id where c.status = 1")
    @Results(id = "coursesMap", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "code", column = "code"),
            @Result(property = "name", column = "name"),
            @Result(property = "description", column = "description"),
            @Result(property = "credit", column = "credit"),
            @Result(property = "teacherId", column = "teacher_id"),
            @Result(property = "capacity", column = "capacity"),
            @Result(property = "semesterId", column = "semester_id"),
            @Result(property = "schedule", column = "schedule"),
            @Result(property = "location", column = "location"),
            @Result(property = "teacherName", column = "teacher_name"),
            @Result(property = "email", column = "email"),
            @Result(property = "semestersName", column = "semesters_name"),
            @Result(property = "startAt", column = "start_at"),
            @Result(property = "endAt", column = "end_at"),
            @Result(property = "status", column = "status"),
            @Result(property = "studyNumber", column = "study_number")
    })
    List<CourseDetailVO> findCoursesDetail();

    @Select("SELECT c.*,u.name as teacher_name,u.email, s.name as semesters_name,s.start_at , s.end_at FROM " +
            "courses c join users u on c.teacher_id = u.id " +
            "join semesters s on s.id = c.semester_id " +
            "where c.id = #{coursesId} and c.status = 1")
    @ResultMap("coursesMap")
    CourseDetailVO findById(@Param("coursesId") String coursesId);

    @Select("SELECT c.*,u.name as teacher_name,u.email, s.name as semesters_name,s.start_at , s.end_at FROM " +
            "courses c join users u on c.teacher_id = u.id " +
            "join semesters s on s.id = c.semester_id " +
            "where u.id = #{userId} and u.status = 1 and c.status = 1")
    @ResultMap("coursesMap")
    List<CourseDetailVO> findCoursesDetailByUserId(@Param("userId") String userId);

    @Select("SELECT c.*,u.name as teacher_name,u.email, s.name as semesters_name,s.start_at , s.end_at FROM " +
            "courses c join users u on c.teacher_id = u.id " +
            "join semesters s on s.id = c.semester_id " +
            "where s.id = #{semesterId} and c.status = 1")
    @ResultMap("coursesMap")
    List<CourseDetailVO> findCoursesDetailBySemesters(@Param("semesterId") String semesterId);

    @SelectProvider(type = CourseSqlProvider.class, method = "buildSearchSql")
    List<CourseVO> findByParam(CourseQueryParam param);

    @Insert("INSERT INTO courses (id, code, name, description, credit, teacher_id, capacity, semester_id, schedule, location , status) " +
            "VALUES (#{id}, #{code}, #{name}, #{description}, #{credit}, #{teacherId}, #{capacity}, #{semesterId}, #{schedule}, #{location} ,#{status})")
    int insert(CourseVO course);


    @Delete("DELETE FROM courses WHERE id=#{id}")
    int delete(@Param("id") String id);

    @Update({
            "<script>",
            "UPDATE courses",
            "<set>",
            "  <if test='code != null'>code = #{code},</if>",
            "  <if test='name != null'>name = #{name},</if>",
            "  <if test='description != null'>description = #{description},</if>",
            "  <if test='credit != null'>credit = #{credit},</if>",
            "  <if test='teacherId != null'>teacher_id = #{teacherId},</if>",
            "  <if test='capacity != null'>capacity = #{capacity},</if>",
            "  <if test='semesterId != null'>semester_id = #{semesterId},</if>",
            "  <if test='schedule != null'>schedule = #{schedule},</if>",
            "  <if test='location != null'>location = #{location},</if>",
            "  <if test='status != null'>location = #{status},</if>",
            "</set>",
            "WHERE id = #{id} and teacher_id = #{teacherId}",
            "</script>"
    })
    int teacherUpdateCourse(CourseVO course, @Param("teacherId") String teacherId);

    @Update("UPDATE courses set study_number = #{studyNumber} where id = #{id}")
    int updateStudyNumber(@Param("id") String id, @Param("studyNumber") int studyNumber);


    @Update("UPDATE courses set status = #{status} where id = #{id}")
    int updateStatus(@Param("id") String id, @Param("status") String status);


    @Select("Select cr.* ,u.name from course_requests cr join courses c on cr.course_id = c.id" +
            " join users u on u.id = cr.student_id " +
            " where c.id = #{id}")
    @Results(id = "courseRequestMap", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "studentId", column = "student_id"),
            @Result(property = "courseId", column = "course_id"),
            @Result(property = "type", column = "type"),
            @Result(property = "reason", column = "reason"),
            @Result(property = "status", column = "status"),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "name", column = "name")
    })
    List<courseRequestDetailVO> findCourseRequestByCourseId(@Param("id") String id);
}
