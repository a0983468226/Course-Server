package com.course.enums;

public enum EnrollmentsStatus {

    ENROLLED("enrolled"),

    PENDING("pending"),

    WITHDRAWN("withdrawn");


    private final String value;

    EnrollmentsStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static EnrollmentsStatus fromValue(String value) {
        for (EnrollmentsStatus status : EnrollmentsStatus.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
