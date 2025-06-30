package com.course.mapper;

import com.course.mapper.vo.MenuVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MenuMapper {

    @Select("SELECT * FROM menu WHERE id = #{id}")
    @Results(id = "basicMap", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "title", column = "title"),
            @Result(property = "description", column = "description"),
            @Result(property = "role", column = "role"),
            @Result(property = "path", column = "path"),
            @Result(property = "icon", column = "icon")
    })

    MenuVO findById(@Param("id") String id);
    @Select("SELECT * FROM menu WHERE role = #{role} order by icon ")
    @ResultMap("basicMap")
    List<MenuVO> findByRole(@Param("role") String role);

    @Insert("INSERT INTO menu (id, title, description, role,path) VALUES (#{id}, #{title}, #{description}, #{role}) , #{path}")
    int insert(MenuVO semester);


    @Delete("DELETE FROM menu WHERE id=#{id}")
    int delete(@Param("id") String id);

    @Update({
            "<script>",
            "UPDATE menu",
            "<set>",
            "  <if test='title != null'>name = #{title},</if>",
            "  <if test='description != null'>start_at = #{description},</if>",
            "  <if test='role != null'>end_at = #{role},</if>",
            "  <if test='path != null'>end_at = #{path},</if>",
            "</set>",
            "WHERE id = #{id}",
            "</script>"
    })
    int update(MenuVO semester);
}
