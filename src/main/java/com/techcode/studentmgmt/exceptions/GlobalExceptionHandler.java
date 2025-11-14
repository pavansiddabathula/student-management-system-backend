package com.techcode.studentmgmt.exceptions;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.techcode.studentmgmt.enums.ErrorCode;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // DTO validation errors (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        log.warn("Validation exception caught: {}", ex.getMessage());

        Map<String, String> fieldErrors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err -> {
            fieldErrors.put(err.getField(), err.getDefaultMessage());
            log.warn("Validation error - Field: {}, Message: {}", err.getField(), err.getDefaultMessage());
        });

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status","FAILURE");
        response.put("errorCode", ErrorCode.VALIDATION_ERROR.getCode());
        response.put("errorMessage", ErrorCode.VALIDATION_ERROR.getMessage());
        response.put("fieldErrors", fieldErrors);
        response.put("timestamp", LocalDateTime.now());

        log.info("Returning validation error response: {}", response);
        return ResponseEntity.status(ErrorCode.VALIDATION_ERROR.getStatus()).body(response);
    }

    // Business validation errors
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> handleValidationException(ValidationException ex) {
        log.warn("Business validation exception caught: {}", ex.getErrors());

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status","FAILURE");
        response.put("errorCode", ErrorCode.VALIDATION_ERROR.getCode());
        response.put("errorMessage", ErrorCode.VALIDATION_ERROR.getMessage());
        response.put("fieldErrors", ex.getErrors());
        response.put("timestamp", LocalDateTime.now());

        log.info("Returning business validation error response: {}", response);
        return ResponseEntity.status(ErrorCode.VALIDATION_ERROR.getStatus()).body(response);
    }

    // Entity not found or other business exception
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<?> handleBusinessException(BusinessException ex) {
        log.warn("Business exception caught: {}", ex.getErrorCode());

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status","FAILURE");
        response.put("errorCode", ex.getErrorCode().getCode());
        response.put("errorMessage", ex.getErrorCode().getMessage());
        response.put("timestamp", LocalDateTime.now());

        log.info("Returning business exception response: {}", response);
        return ResponseEntity.status(ex.getErrorCode().getStatus()).body(response);
    }

    // Catch-all unexpected exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception ex) {
        log.error("Unexpected exception caught: ", ex);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status","FAILURE");
        response.put("errorCode", ErrorCode.INTERNAL_SERVER_ERROR.getCode());
        response.put("errorMessage", ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
        response.put("timestamp", LocalDateTime.now());

        log.info("Returning internal server error response: {}", response);
        return ResponseEntity.status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus()).body(response);
    }
}
