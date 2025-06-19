package com.course.mapper;

import com.course.mapper.vo.CourseRequestVO;
import org.apache.ibatis.annotations.*;

@Mapper
public interface CourseRequestsMapper {

    @Select("SELECT * FROM course_requests WHERE id = #{id}")
    @Results(id = "basicMap", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "studentId", column = "student_id"),
            @Result(property = "courseId", column = "course_id"),
            @Result(property = "type", column = "type"),
            @Result(property = "reason", column = "reason"),
            @Result(property = "status", column = "status"),
            @Result(property = "createdAt", column = "created_at")
    })
    CourseRequestVO findById(@Param("id") String id);


    @Insert("INSERT INTO course_requests (id, student_id, course_id, type, reason, status, created_at) " +
            "VALUES (#{id}, #{studentId}, #{courseId}, #{type}, #{reason}, #{status}, #{createdAt})")
    int insert(CourseRequestVO request);

    @Delete("DELETE FROM course_requests WHERE id=#{id}")
    int delete(@Param("id") String id);

    @Update("Update course_requests set type =#{type} , reason = #{reason} where id = #{id}")
    int update(CourseRequestVO request);

    @Update("Update course_requests set status =#{status}  where id = #{id}")
    int updateStatusById(@Param("status") String status, @Param("id") String id);
}
