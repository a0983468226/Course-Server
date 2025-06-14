package com.course.mapper;

import com.course.mapper.vo.CourseVO;
import org.apache.ibatis.annotations.*;

@Mapper
public interface CoursesMapper {

    @Select("SELECT * FROM courses WHERE id = #{id}")
    @Results(id = "basicMap", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "code", column = "code"),
            @Result(property = "name", column = "name"),
            @Result(property = "description", column = "description"),
            @Result(property = "credit", column = "credit"),
            @Result(property = "teacherId", column = "teacher_id"),
            @Result(property = "capacity", column = "capacity"),
            @Result(property = "semesterId", column = "semester_id"),
            @Result(property = "schedule", column = "schedule"),
            @Result(property = "location", column = "location")
    })
    CourseVO findById(@Param("id") String id);

    @Insert("INSERT INTO courses (id, code, name, description, credit, teacher_id, capacity, semester_id, schedule, location) " +
            "VALUES (#{id}, #{code}, #{name}, #{description}, #{credit}, #{teacherId}, #{capacity}, #{semesterId}, #{schedule}, #{location})")
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
            "</set>",
            "WHERE id = #{id}",
            "</script>"
    })
    int update(CourseVO course);
}
