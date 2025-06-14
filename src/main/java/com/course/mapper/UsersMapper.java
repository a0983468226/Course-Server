package com.course.mapper;

import com.course.mapper.vo.UserVO;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UsersMapper {

    @Select("SELECT * FROM users WHERE id = #{id}")
    @Results(id = "basicMap", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "username", column = "username"),
            @Result(property = "passwordHash", column = "password_hash"),
            @Result(property = "name", column = "name"),
            @Result(property = "email", column = "email"),
            @Result(property = "role", column = "role"),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "status" , column = "status")
    })
    UserVO findById(@Param("id") String id);


    @Select("SELECT * FROM users WHERE username = #{username} and status = '1'")
    @ResultMap("basicMap")
    UserVO findByUsername(@Param("username") String username);


    @Insert("INSERT INTO users (id, username, password_hash, name, email, role, created_at , status) " +
            "VALUES (#{id}, #{username}, #{passwordHash}, #{name}, #{email}, #{role}, #{createdAt} ,#{status})")
    int insert(UserVO user);

    @Delete("DELETE FROM users WHERE id=#{id}")
    int delete(@Param("id") String id);

    @Update({
            "<script>",
            "UPDATE users",
            "<set>",
            "  <if test='username != null'>username = #{username},</if>",
            "  <if test='passwordHash != null'>password_hash = #{passwordHash},</if>",
            "  <if test='name != null'>name = #{name},</if>",
            "  <if test='email != null'>email = #{email},</if>",
            "  <if test='role != null'>role = #{role},</if>",
            "  <if test='status != null'>role = #{status},</if>",
            "</set>",
            "WHERE id = #{id}",
            "</script>"
    })
    int update(UserVO user);
}
