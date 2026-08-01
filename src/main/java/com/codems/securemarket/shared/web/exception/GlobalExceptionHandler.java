package com.codems.securemarket.shared.web.exception;

import com.codems.securemarket.shared.exception.BaseException;
import com.codems.securemarket.shared.exception.ErrorCategory;
import com.codems.securemarket.shared.event.UnexpectedApplicationErrorEvent;
import com.codems.securemarket.shared.web.response.BaseResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.context.ApplicationEventPublisher;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final ApplicationEventPublisher eventPublisher;

    public GlobalExceptionHandler(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<BaseResponse<Object>> handleBaseException(BaseException exception) {
        log.warn("Handled application exception [{}]: {}", exception.getCode(), exception.getMessage());
        return build(
                exception.getCode(),
                toHttpStatus(exception.getCategory()),
                exception.getMessage(),
                exception.getValidationErrors(),
                exception.getDetails());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<Object>> handleValidation(MethodArgumentNotValidException exception) {
        Map<String, String> fieldErrors = new LinkedHashMap<>();

        exception.getBindingResult().getFieldErrors().forEach(error -> fieldErrors.merge(
                error.getField(),
                String.valueOf(error.getDefaultMessage()),
                (first, second) -> first + ", " + second));
        return build(CommonErrorType.VALIDATION_FAILED, fieldErrors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<BaseResponse<Object>> handleMalformedRequest(HttpMessageNotReadableException exception) {
        log.debug("Malformed request handled", exception);
        return build(CommonErrorType.MALFORMED_REQUEST, Map.of());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<BaseResponse<Object>> handleAuthentication(AuthenticationException exception) {
        log.debug("Authentication exception handled");
        return build(CommonErrorType.UNAUTHORIZED, Map.of());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<BaseResponse<Object>> handleAccessDenied(AccessDeniedException exception) {
        log.debug("Access denied handled");
        return build(CommonErrorType.FORBIDDEN, Map.of());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<Object>> handleUnexpected(
            Exception exception,
            HttpServletRequest request
    ) {
        log.error("Unexpected error", exception);
        eventPublisher.publishEvent(new UnexpectedApplicationErrorEvent(
                UUID.randomUUID(),
                exception.getClass().getSimpleName(),
                request.getMethod(),
                request.getRequestURI(),
                Instant.now()
        ));
        return build(CommonErrorType.INTERNAL_ERROR, Map.of());
    }

    private ResponseEntity<BaseResponse<Object>> build(
            CommonErrorType errorType,
            Map<String, String> fieldErrors) {
        return build(
                errorType.code(),
                errorType.status(),
                errorType.message(),
                fieldErrors,
                Map.of());
    }

    private ResponseEntity<BaseResponse<Object>> build(
            String code,
            HttpStatus status,
            String message,
            Map<String, String> fieldErrors,
            Map<String, Object> details) {
        return ResponseEntity.status(status)
                .body(BaseResponse.error(code, message, status, fieldErrors, details));
    }

    private HttpStatus toHttpStatus(ErrorCategory category) {
        return switch (category) {
            case VALIDATION -> HttpStatus.BAD_REQUEST;
            case NOT_FOUND -> HttpStatus.NOT_FOUND;
            case CONFLICT, BUSINESS_RULE -> HttpStatus.CONFLICT;
            case UNAUTHORIZED -> HttpStatus.UNAUTHORIZED;
            case FORBIDDEN -> HttpStatus.FORBIDDEN;
        };
    }
}
