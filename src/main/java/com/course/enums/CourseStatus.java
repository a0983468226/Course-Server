package com.course.enums;

public enum CourseStatus {

    DISABLE("disable","0"),

    ENABLE("enable","1"),

    PENDING("pending","2");

    private final String name;
    private final String value;

    CourseStatus(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return value;
    }

    public static CourseStatus fromValue(String value) {
        for (CourseStatus status : CourseStatus.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }

    public static CourseStatus fromName(String name) {
        for (CourseStatus status : CourseStatus.values()) {
            if (status.name.equalsIgnoreCase(name)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + name);
    }
}
