package com.codems.securemarket.shared.web.exception;

import org.springframework.http.HttpStatus;

public enum CommonErrorType {
    VALIDATION_FAILED("COMMON_VALIDATION_FAILED", HttpStatus.BAD_REQUEST, "Request validation failed"),
    MALFORMED_REQUEST("COMMON_MALFORMED_REQUEST", HttpStatus.BAD_REQUEST, "Request body is invalid"),
    UNAUTHORIZED("COMMON_UNAUTHORIZED", HttpStatus.UNAUTHORIZED, "Authentication is required"),
    FORBIDDEN("COMMON_FORBIDDEN", HttpStatus.FORBIDDEN, "You are not allowed to perform this operation"),
    INTERNAL_ERROR("COMMON_INTERNAL_ERROR", HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");

    private final String code;
    private final HttpStatus status;
    private final String message;

    CommonErrorType(String code, HttpStatus status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }

    public String code() {
        return code;
    }

    public HttpStatus status() {
        return status;
    }

    public String message() {
        return message;
    }
}

