package com.course.util;

import com.course.exception.ServiceException;
import com.course.model.AbstractResponseData;
import com.course.model.BasicResponse;

public class ResponseUtil {

    @FunctionalInterface
    public interface SupplierWithException<T> {
        T get() throws Exception;
    }

    public static <T extends AbstractResponseData> BasicResponse<T> execute(
            SupplierWithException<T> action,
            String successMsg,
            String errorMsg
    ) {
        BasicResponse<T> response = new BasicResponse<>();

        try {
            T result = action.get();
            response.setData(result);
            response.setMessage(successMsg);
            response.setSuccess(true);
        } catch (ServiceException se) {
            response.setSuccess(false);
            response.setMessage(se.getMessage() != null ? se.getMessage() : errorMsg);
            response.setCode(se.getCode());
            response.setErrorType("ServiceException");
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage(errorMsg != null ? errorMsg : "執行失敗");
            response.setCode(500);
            response.setErrorType(e.getClass().getSimpleName());
            e.printStackTrace();
        }

        return response;
    }
}
