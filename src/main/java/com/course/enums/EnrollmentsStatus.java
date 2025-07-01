package com.course.enums;

public enum EnrollmentsStatus {

    ENROLLED("enrolled", "選課成功"),

    PENDING("pending", "審核中"),

    WITHDRAWN("withdrawn", "選課失敗");


    private final String value;

    private final String name;

    EnrollmentsStatus(String value, String name) {
        this.value = value;
        this.name = name;
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

    public static EnrollmentsStatus fromValue(String value) {
        for (EnrollmentsStatus status : EnrollmentsStatus.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }

    public static EnrollmentsStatus fromName(String name) {
        for (EnrollmentsStatus status : EnrollmentsStatus.values()) {
            if (status.name.equalsIgnoreCase(name)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + name);
    }
}
