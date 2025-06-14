package com.course.enums;

public enum CourseRequestsStatus {

    PENDING("pending"),

    APPROVED("approved"),

    REJECTED("rejected");


    private final String value;

    CourseRequestsStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static CourseRequestsStatus fromValue(String value) {
        for (CourseRequestsStatus status : CourseRequestsStatus.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
