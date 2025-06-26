package com.course.mapper;

import com.course.mapper.vo.SemesterVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SemestersMapper {


    @Select("SELECT * FROM semesters WHERE id = #{id}")
    @Results(id = "basicMap", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "startAt", column = "start_at"),
            @Result(property = "endAt", column = "end_at")
    })
    SemesterVO findById(@Param("id") String id);

    @Select("SELECT * FROM semesters ")
    @ResultMap("basicMap")
    List<SemesterVO> findAll();

    @Insert("INSERT INTO semesters (id, name, start_at, end_at) VALUES (#{id}, #{name}, #{startAt}, #{endAt})")
    int insert(SemesterVO semester);


    @Delete("DELETE FROM semesters WHERE id=#{id}")
    int delete(@Param("id") String id);

    @Update({
            "<script>",
            "UPDATE semesters",
            "<set>",
            "  <if test='name != null'>name = #{name},</if>",
            "  <if test='startAt != null'>start_at = #{startAt},</if>",
            "  <if test='endAt != null'>end_at = #{endAt},</if>",
            "</set>",
            "WHERE id = #{id}",
            "</script>"
    })
    int update(SemesterVO semester);
}
