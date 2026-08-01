package com.codems.securemarket.shared.web.response;

import java.util.Map;

public record ErrorResponse(
        String code,
        String message,
        int status,
        Map<String, String> fieldErrors,
        Map<String, Object> details
) {
    public ErrorResponse {
        fieldErrors = fieldErrors == null ? Map.of() : Map.copyOf(fieldErrors);
        details = details == null ? Map.of() : Map.copyOf(details);
    }
}

