package com.course.util;

import com.course.model.AbstractResponseData;
import com.course.model.BasicResponse;

public class ResponseUtil {

    @FunctionalInterface
    public interface SupplierWithException<T> {
        T get() throws Exception;
    }

    public static <T extends AbstractResponseData> BasicResponse<T> execute(SupplierWithException<T> action, String successMsg, String errorMsg) {
        BasicResponse<T> response = new BasicResponse<>();
        try {
            T result = action.get();
            response.setData(result);
            response.setMessage(successMsg);
            response.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            response.setMessage(errorMsg);
            response.setSuccess(false);
        }
        return response;
    }
}
