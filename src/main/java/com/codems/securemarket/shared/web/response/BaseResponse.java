package com.codems.securemarket.shared.web.response;

import java.time.Instant;
import java.util.Map;
import org.springframework.http.HttpStatus;

public record BaseResponse<T>(
        boolean success,
        T data,
        ErrorResponse error,
        Instant timestamp
) {
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(true, data, null, Instant.now());
    }

    public static <T> BaseResponse<T> error(
            String code,
            String message,
            HttpStatus status,
            Map<String, String> fieldErrors,
            Map<String, Object> details
    ) {
        var error = new ErrorResponse(
                code,
                message,
                status.value(),
                fieldErrors,
                details
        );
        return new BaseResponse<>(false, null, error, Instant.now());
    }
}

