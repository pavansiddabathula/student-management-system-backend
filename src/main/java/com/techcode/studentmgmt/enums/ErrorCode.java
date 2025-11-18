package com.techcode.studentmgmt.enums;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum ErrorCode {

    VALIDATION_ERROR("E001", "Validation failed", HttpStatus.BAD_REQUEST),

    STUDENT_NOT_FOUND("E002", "Student with roll number '%s' not found.", HttpStatus.NOT_FOUND),
    
    STUDENT_NOY_FOUND_BYNAME("E002", "Student with name '%s' not found.", HttpStatus.NOT_FOUND),

    INTERNAL_SERVER_ERROR("E500", "Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR),
    
	DATABASE_ERROR("E503", "Database is currently not available. Please try again.", HttpStatus.SERVICE_UNAVAILABLE);

	   

    private final String code;
    private final String message;
    private final HttpStatus status;

    ErrorCode(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    //dynamic message formatting
    public String format(Object... args) {
        return String.format(this.message, args);
    }
}
