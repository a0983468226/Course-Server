package com.course.mapper;

import com.course.mapper.vo.CourseReviewVO;
import org.apache.ibatis.annotations.*;

@Mapper
public interface CourseReviewsMapper {

    @Mapper
    public interface CourseReviewMapper {

        @Select("SELECT * FROM course_reviews WHERE id = #{id}")
        @Results(id = "basicMap", value = {
                @Result(property = "id", column = "id"),
                @Result(property = "courseId", column = "course_id"),
                @Result(property = "studentId", column = "student_id"),
                @Result(property = "rating", column = "rating"),
                @Result(property = "comment", column = "comment"),
                @Result(property = "createdAt", column = "created_at")
        })
        CourseReviewVO findById(@Param("id") String id);
    }

    @Insert("INSERT INTO course_reviews (id, course_id, student_id, rating, comment, created_at) " +
            "VALUES (#{id}, #{courseId}, #{studentId}, #{rating}, #{comment}, #{createdAt})")
    int insert(CourseReviewVO review);


    @Delete("DELETE FROM course_reviews WHERE id=#{id}")
    int delete(@Param("id") String id);

    @Update({
            "<script>" +
                    "UPDATE course_reviews" +
                    "<set>" +
                    "  <if test='courseId != null'> course_id = #{courseId},</if>" +
                    "  <if test='studentId != null'> student_id = #{studentId},</if>" +
                    "  <if test='rating != null'> rating = #{rating},</if>" +
                    "  <if test='comment != null'> comment = #{comment},</if>" +
                    "</set>",
            " WHERE id = #{id}",
            "</script>"
    })
    int update(CourseReviewVO review);
}
