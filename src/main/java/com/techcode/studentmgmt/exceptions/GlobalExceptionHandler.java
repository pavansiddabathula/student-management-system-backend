package com.techcode.studentmgmt.exceptions;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.exception.SQLGrammarException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.techcode.studentmgmt.dto.responsedto.ErrorResponse;
import com.techcode.studentmgmt.enums.ErrorCode;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	/*
	 * 1. DTO Validation Errors (@Valid)
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {

		log.warn("Validation exception caught: {}", ex.getMessage());

		Map<String, String> fieldErrors = new LinkedHashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(err -> {
			fieldErrors.put(err.getField(), err.getDefaultMessage());
		});

		ErrorResponse response = ErrorResponse.builder().status("FAILURE")
				.errorCode(ErrorCode.VALIDATION_ERROR.getCode()).errorMessage(ErrorCode.VALIDATION_ERROR.getMessage())
				.fieldErrors(fieldErrors).timestamp(LocalDateTime.now()).build();

		return ResponseEntity.status(ErrorCode.VALIDATION_ERROR.getStatus()).body(response);
	}

	/*
	 * 2. Business Validation Errors (duplicate checks)
	 */
	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<?> handleValidationException(ValidationException ex) {

		log.warn("Business validation exception: {}", ex.getErrors());

		ErrorResponse response = ErrorResponse.builder().status("FAILURE")
				.errorCode(ErrorCode.VALIDATION_ERROR.getCode()).errorMessage("Validation failed")
				.fieldErrors(ex.getErrors()).timestamp(LocalDateTime.now()).build();

		return ResponseEntity.status(ErrorCode.VALIDATION_ERROR.getStatus()).body(response);
	}

	/*
	 * 3. BusinessException (dynamic messages)
	 */
	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<?> handleBusinessException(BusinessException ex) {

		log.warn("Business exception caught: {}", ex.getErrorCode());

		ErrorResponse response = ErrorResponse.builder().status("FAILURE").errorCode(ex.getErrorCode().getCode())
				.errorMessage(ex.getFormattedMessage()).timestamp(LocalDateTime.now()).build();

		return ResponseEntity.status(ex.getErrorCode().getStatus()).body(response);
	}

	/*
	 * -- 4. Spring Database Exception (DB down, query failure)
	 * ----------------------------------------------------
	 */


	@ExceptionHandler({ CannotCreateTransactionException.class, SQLGrammarException.class,
			JDBCConnectionException.class, JpaSystemException.class, DataAccessException.class })
	public ResponseEntity<?> handleDBErrors(Exception ex) {

		log.error("Database error: {}", ex.getMessage());

		ErrorResponse response = ErrorResponse.builder().status("FAILURE").errorCode(ErrorCode.DATABASE_ERROR.getCode())
				.errorMessage(ErrorCode.DATABASE_ERROR.getMessage()).timestamp(LocalDateTime.now()).build();

		return ResponseEntity.status(ErrorCode.DATABASE_ERROR.getStatus()).body(response);
	}

	/*
	 * 5. Generic / Unexpected Exceptions
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleGlobalException(Exception ex) {

		log.error("Unexpected exception caught: ", ex);

		ErrorResponse response = ErrorResponse.builder().status("FAILURE")
				.errorCode(ErrorCode.INTERNAL_SERVER_ERROR.getCode())
				.errorMessage(ErrorCode.INTERNAL_SERVER_ERROR.getMessage()).timestamp(LocalDateTime.now()).build();

		return ResponseEntity.status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus()).body(response);
	}

}
