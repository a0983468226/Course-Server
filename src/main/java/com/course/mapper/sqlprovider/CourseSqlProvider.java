package com.course.mapper.sqlprovider;

import com.course.model.courses.CourseQueryParam;
import org.apache.commons.lang3.StringUtils;

public class CourseSqlProvider {

    public String buildSearchSql(CourseQueryParam param) {
        StringBuilder sql = new StringBuilder("SELECT * FROM courses WHERE 1=1");

        if (StringUtils.isNotBlank(param.getId())) {
            sql.append(" AND id = #{id}");
        }

        if (StringUtils.isNotBlank(param.getCode())) {
            sql.append(" AND code = #{code}");
        }

        if (StringUtils.isNotBlank(param.getName())) {
            sql.append(" AND name LIKE CONCAT('%', #{name}, '%')");
        }

        if (StringUtils.isNotBlank(param.getDescription())) {
            sql.append(" AND description LIKE CONCAT('%', #{description}, '%')");
        }

        if (param.getCredit() != null) {
            sql.append(" AND credit = #{credit}");
        }

        if (StringUtils.isNotBlank(param.getTeacherId())) {
            sql.append(" AND teacher_id = #{teacherId}");
        }

        if (param.getCapacity() != null) {
            sql.append(" AND capacity = #{capacity}");
        }

        if (StringUtils.isNotBlank(param.getSemesterId())) {
            sql.append(" AND semester_id = #{semesterId}");
        }

        if (StringUtils.isNotBlank(param.getSchedule())) {
            sql.append(" AND schedule LIKE CONCAT('%', #{schedule}, '%')");
        }

        if (StringUtils.isNotBlank(param.getLocation())) {
            sql.append(" AND location LIKE CONCAT('%', #{location}, '%')");
        }

        return sql.toString();
    }
}
