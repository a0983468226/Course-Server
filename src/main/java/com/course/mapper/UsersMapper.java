package com.course.mapper;

import com.course.mapper.vo.UserVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

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
            @Result(property = "status" , column = "status"),
            @Result(property = "isFirstLogin" , column = "is_first_login"),
            @Result(property = "updateAt" , column = "update_at"),
            @Result(property = "lastLoginTime" , column = "last_login_time")
    })
    UserVO findById(@Param("id") String id);

    @Select("SELECT * FROM users ")
    @ResultMap("basicMap")
    List<UserVO> getAllUsers();


    @Select("SELECT * FROM users WHERE username = #{username} and status = '1'")
    @ResultMap("basicMap")
    UserVO findByUsername(@Param("username") String username);


    @Insert("INSERT INTO users (id, username, password_hash, name, email, role, created_at , status , is_first_login , , last_login_time) " +
            "VALUES (#{id}, #{username}, #{passwordHash}, #{name}, #{email}, #{role}, #{createdAt} ,#{status} , #{isFirstLogin} ,#{updateAt} , #{lastLoginTime})")
    int insert(UserVO user);

    @Update("Update  users  set status = 0 WHERE id=#{id}")
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
            "  <if test='isFirstLogin != null'>is_first_login = #{isFirstLogin},</if>",
            "  <if test='updateAt != null'>update_at = #{updateAt},</if>",
            "  <if test='lastLoginTime != null'>last_login_time = #{lastLoginTime},</if>",
            "</set>",
            "WHERE id = #{id}",
            "</script>"
    })
    int update(UserVO user);
}
