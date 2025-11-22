package com.techcode.studentmgmt.constants;

import org.springframework.http.HttpStatus;

import lombok.Getter;


@Getter
public enum ErrorCodeEnums {

    VALIDATION_ERROR("E001", "Validation failed", HttpStatus.BAD_REQUEST),

    STUDENT_NOT_FOUND("E002", "Student with roll number '%s' not found.", HttpStatus.NOT_FOUND),

    STUDENT_NOY_FOUND_BYNAME("E002", "Student with name '%s' not found.", HttpStatus.NOT_FOUND),

    ADMIN_NOT_FOUND("E003", "Admin with Admin ID '%s' not found.", HttpStatus.NOT_FOUND),

    ADMIN_EMAIL_EXISTS("E004", "Email '%s' already exists.", HttpStatus.BAD_REQUEST),

    ADMIN_PHONE_EXISTS("E005", "Phone number '%s' already exists.", HttpStatus.BAD_REQUEST),
    
    UNAUTHORIZED("E401", "Token is missing or invalid. Please login again.", HttpStatus.UNAUTHORIZED),
    ACCESS_DENIED("E403", "You do not have permission to access this resource.", HttpStatus.FORBIDDEN),
   
    INTERNAL_SERVER_ERROR("E500", "Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR),

    DATABASE_ERROR("E503", "Database is currently not available. Please try again.", HttpStatus.SERVICE_UNAVAILABLE);

    private final String code;
    private final String message;
    private final HttpStatus status;

    ErrorCodeEnums(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    public String format(Object... args) {
        return String.format(this.message, args);
    }


}
