package com.course.enums;

public enum CourseRequestsType {

    ADD("add"),

    DROP("drop");

    private final String value;

    CourseRequestsType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static CourseRequestsType fromValue(String value) {
        for (CourseRequestsType type : CourseRequestsType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}

