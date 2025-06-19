package com.course.exception;

public class ServiceException extends RuntimeException {
    private final int code;

    public ServiceException(String message) {
        super(message);
        this.code = 400; // 預設錯誤代碼
    }

    public ServiceException(int code, String message) {
        super(message);
        this.code = code;
    }

    public ServiceException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
