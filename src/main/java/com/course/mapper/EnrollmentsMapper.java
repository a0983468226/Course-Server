package com.course.mapper;

import com.course.mapper.vo.EnrollmentStudentVO;
import com.course.mapper.vo.EnrollmentVO;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

@Mapper
public interface EnrollmentsMapper {

    @Select("SELECT * FROM enrollments WHERE id = #{id}")
    @Results(id = "basicMap", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "studentId", column = "student_id"),
            @Result(property = "courseId", column = "course_id"),
            @Result(property = "status", column = "status"),
            @Result(property = "enrolledAt", column = "enrolled_at")
    })
    EnrollmentVO findById(@Param("id") String id);


    @Select("SELECT e.* , u.id as student_id , u.student_name as studentName " +
            " FROM enrollments e join users on u.id = e.student_id" +
            " join courses c on c.id = e.course_id " +
            " where u.status = 1 and c.id = #{id}")
    @Results(id = "studentMap", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "studentId", column = "student_id"),
            @Result(property = "studentName", column = "student_name"),
            @Result(property = "email", column = "email"),
            @Result(property = "status", column = "status"),
            @Result(property = "enrolledAt", column = "enrolled_at")
    })
    List<EnrollmentStudentVO> findByCoursesId(@Param("id") String id);

    @Select("SELECT * FROM enrollments WHERE student_id = #{studentId} and course_id =#{courseId} ")
    @ResultMap("basicMap")
    List<EnrollmentVO> findByStudentAndCourse(@Param("studentId") String studentId, @Param("courseId") String courseId);


    @Update({
            "<script>",
            "UPDATE enrollments",
            "<set>",
            "  <if test='studentId != null'>student_id = #{studentId},</if>",
            "  <if test='courseId != null'>course_id = #{courseId},</if>",
            "  <if test='status != null'>status = #{status},</if>",
            "  <if test='enrolledAt != null'>enrolled_at = #{enrolledAt},</if>",
            "</set>",
            "WHERE id = #{id}",
            "</script>"
    })
    int update(EnrollmentVO enrollment);

    @Update("Update set status = #{status} where id = #{id}")
    int updateByStatus(@Param("id") String id, @Param("status") String status);


    @Insert("INSERT INTO enrollments (id, student_id, course_id, status, enrolled_at) " +
            "VALUES (#{id}, #{studentId}, #{courseId}, #{status}, #{enrolledAt})")
    int insert(EnrollmentVO enrollment);


    @Delete("DELETE FROM enrollments WHERE id=#{id}")
    int delete(@Param("id") String id);
}
