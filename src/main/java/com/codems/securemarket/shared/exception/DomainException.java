package com.codems.securemarket.shared.exception;

import java.util.Map;

public abstract class DomainException extends BaseException {

    protected DomainException(String code, ErrorCategory category, String message) {
        super(code, category, message);
    }

    protected DomainException(
            String code,
            ErrorCategory category,
            String message,
            Map<String, Object> details) {
        super(code, category, message, Map.of(), details);
    }
}
