package com.techcode.studentmgmt.exceptions;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.exception.SQLGrammarException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.techcode.studentmgmt.constants.ErrorCodeEnums;
import com.techcode.studentmgmt.dto.responsedto.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j

public class GlobalExceptionHandler {

	// Handle validation errors for invalid request fields
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {

		log.warn("Validation exception caught: {}", ex.getMessage());

		Map<String, String> fieldErrors = new LinkedHashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(err -> {
			fieldErrors.put(err.getField(), err.getDefaultMessage());
		});

		ErrorResponse response = ErrorResponse.builder()
				.status("FAILURE")
				.errorCode(ErrorCodeEnums.VALIDATION_ERROR.getCode())
				.errorMessage(ErrorCodeEnums.VALIDATION_ERROR.getMessage())
				.fieldErrors(fieldErrors)
				.timestamp(LocalDateTime.now()).build();

		return ResponseEntity.status(ErrorCodeEnums.VALIDATION_ERROR.getStatus()).body(response);
	}

	// Handle custom validation exceptions (business rule validations)
	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<?> handleValidationException(ValidationException ex) {

		log.warn("Business validation exception: {}", ex.getErrors());

		ErrorResponse response = ErrorResponse.builder().status("FAILURE")
				.errorCode(ErrorCodeEnums.VALIDATION_ERROR.getCode()).errorMessage("Validation failed")
				.fieldErrors(ex.getErrors()).timestamp(LocalDateTime.now()).build();

		return ResponseEntity.status(ErrorCodeEnums.VALIDATION_ERROR.getStatus()).body(response);
	}

	// Handle business logic exceptions with dynamic messages
	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<?> handleBusinessException(BusinessException ex) {

		log.warn("Business exception caught: {}", ex.getErrorCode());

		ErrorResponse response = ErrorResponse.builder().status("FAILURE").errorCode(ex.getErrorCode().getCode())
				.errorMessage(ex.getMessage()).timestamp(LocalDateTime.now()).build();

		return ResponseEntity.status(ex.getErrorCode().getStatus()).body(response);
	}

	// Handle database-related failures (DB down, wrong queries, connectivity errors)
	@ExceptionHandler({ CannotCreateTransactionException.class, SQLGrammarException.class,
			JDBCConnectionException.class, JpaSystemException.class, DataAccessException.class })
	public ResponseEntity<?> handleDBErrors(Exception ex) {

		log.error("Database error: {}", ex.getMessage());

		ErrorResponse response = ErrorResponse.builder().status("FAILURE").errorCode(ErrorCodeEnums.DATABASE_ERROR.getCode())
				.errorMessage(ErrorCodeEnums.DATABASE_ERROR.getMessage()).timestamp(LocalDateTime.now()).build();

		return ResponseEntity.status(ErrorCodeEnums.DATABASE_ERROR.getStatus()).body(response);
	}
	
	// Handle database-related failures (DB down, wrong queries, connectivity errors)
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<?> handleAccessDenied(AccessDeniedException ex) {

	    log.warn("Access denied exception intercepted: {}", ex.getMessage());

	    ErrorResponse response = ErrorResponse.builder()
	            .status("FAILURE")
	            .errorCode(ErrorCodeEnums.ACCESS_DENIED.getCode())
	            .errorMessage(ErrorCodeEnums.ACCESS_DENIED.getMessage())
	            .timestamp(LocalDateTime.now())
	            .build();

	    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
	}


	// Handle Unexpected exceptions
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleGlobalException(Exception ex) {

		log.error("Unexpected exception caught: ", ex);

		ErrorResponse response = ErrorResponse.builder().status("FAILURE")
				.errorCode(ErrorCodeEnums.INTERNAL_SERVER_ERROR.getCode())
				.errorMessage(ErrorCodeEnums.INTERNAL_SERVER_ERROR.getMessage()).timestamp(LocalDateTime.now()).build();

		return ResponseEntity.status(ErrorCodeEnums.INTERNAL_SERVER_ERROR.getStatus()).body(response);

	}
	
  /*  @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationErrors(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        System.out.println("VALIDATION ERRORS: " + errors);

        return ResponseEntity.badRequest().body(errors);
    }*/
	
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Internal exception handled: " + ex.getMessage());
    }
    

}
