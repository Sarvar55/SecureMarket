package com.codems.securemarket.shared.exception;

import java.util.Map;
import java.util.Objects;

public abstract class BaseException extends RuntimeException {

    private final String code;
    private final ErrorCategory category;
    private final Map<String, String> validationErrors;
    private final Map<String, Object> details;

    protected BaseException(String code, ErrorCategory category, String message) {
        this(code, category, message, Map.of(), Map.of());
    }

    protected BaseException(
            String code,
            ErrorCategory category,
            String message,
            Map<String, String> validationErrors,
            Map<String, Object> details) {
        super(Objects.requireNonNull(message, "message must not be null"));
        this.code = Objects.requireNonNull(code, "code must not be null");
        this.category = Objects.requireNonNull(category, "category must not be null");
        this.validationErrors = validationErrors == null ? Map.of() : Map.copyOf(validationErrors);
        this.details = details == null ? Map.of() : Map.copyOf(details);
    }

    public String getCode() {
        return code;
    }

    public ErrorCategory getCategory() {
        return category;
    }

    public Map<String, String> getValidationErrors() {
        return validationErrors;
    }

    public Map<String, Object> getDetails() {
        return details;
    }
}
