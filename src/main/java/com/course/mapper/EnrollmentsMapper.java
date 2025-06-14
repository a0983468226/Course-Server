package com.course.mapper;

import com.course.mapper.vo.EnrollmentVO;
import org.apache.ibatis.annotations.*;

@Mapper
public interface EnrollmentsMapper {
    @Mapper
    public interface EnrollmentMapper {

        @Select("SELECT * FROM enrollments WHERE id = #{id}")
        @Results(id = "basicMap", value = {
                @Result(property = "id", column = "id"),
                @Result(property = "studentId", column = "student_id"),
                @Result(property = "courseId", column = "course_id"),
                @Result(property = "status", column = "status"),
                @Result(property = "enrolledAt", column = "enrolled_at")
        })
        EnrollmentVO findById(@Param("id") String id);
    }

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

    @Insert("INSERT INTO enrollments (id, student_id, course_id, status, enrolled_at) " +
            "VALUES (#{id}, #{studentId}, #{courseId}, #{status}, #{enrolledAt})")
    int insert(EnrollmentVO enrollment);


    @Delete("DELETE FROM enrollments WHERE id=#{id}")
    int delete(@Param("id") String id);
}
