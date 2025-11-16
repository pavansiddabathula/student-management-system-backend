package com.techcode.studentmgmt.exceptions;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.techcode.studentmgmt.constants.ErrorMessageConstants;
import com.techcode.studentmgmt.constants.ResponseKeys;
import com.techcode.studentmgmt.enums.ErrorCode;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        log.warn("Validation exception caught: {}", ex.getMessage());

        Map<String, String> fieldErrors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err -> {
            fieldErrors.put(err.getField(), err.getDefaultMessage());
            log.warn("Validation error - Field: {}, Message: {}", err.getField(), err.getDefaultMessage());
        });

        Map<String, Object> response = new LinkedHashMap<>();
        response.put(ResponseKeys.STATUS, "FAILURE");
        response.put(ResponseKeys.ERROR_CODE, ErrorCode.VALIDATION_ERROR.getCode());
        response.put(ResponseKeys.ERROR_MESSAGE, ErrorCode.VALIDATION_ERROR.getMessage());
        response.put(ResponseKeys.FIELD_ERRORS, fieldErrors);
        response.put(ResponseKeys.TIMESTAMP, LocalDateTime.now());

        return ResponseEntity.status(ErrorCode.VALIDATION_ERROR.getStatus()).body(response);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> handleValidationException(ValidationException ex) {
        log.warn("Business validation exception: {}", ex.getErrors());

        Map<String, Object> response = new LinkedHashMap<>();
        response.put(ResponseKeys.STATUS,ErrorMessageConstants.FAILUR_STRING);	
        response.put(ResponseKeys.ERROR_CODE, ErrorCode.VALIDATION_ERROR.getCode());
        response.put(ResponseKeys.ERROR_MESSAGE, ErrorCode.VALIDATION_ERROR.getMessage());
        response.put(ResponseKeys.FIELD_ERRORS, ex.getErrors());
        response.put(ResponseKeys.TIMESTAMP, LocalDateTime.now());

        return ResponseEntity.status(ErrorCode.VALIDATION_ERROR.getStatus()).body(response);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<?> handleBusinessException(BusinessException ex) {
        log.warn("Business exception caught: {}", ex.getErrorCode());

        Map<String, Object> response = new LinkedHashMap<>();
        response.put(ResponseKeys.STATUS,ErrorMessageConstants.FAILUR_STRING);
        response.put(ResponseKeys.ERROR_CODE, ex.getErrorCode().getCode());
        response.put(ResponseKeys.ERROR_MESSAGE, ex.getErrorCode().getMessage());
        response.put(ResponseKeys.TIMESTAMP, LocalDateTime.now());

        return ResponseEntity.status(ex.getErrorCode().getStatus()).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception ex) {
        log.error("Unexpected exception caught: ", ex);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put(ResponseKeys.STATUS, ErrorMessageConstants.FAILUR_STRING);
        response.put(ResponseKeys.ERROR_CODE, ErrorCode.INTERNAL_SERVER_ERROR.getCode());
        response.put(ResponseKeys.ERROR_MESSAGE, ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
        response.put(ResponseKeys.TIMESTAMP, LocalDateTime.now());

        return ResponseEntity.status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus()).body(response);
    }
}
