package com.techcode.studentmgmt.enums;


import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum ErrorCode {
    VALIDATION_ERROR("E001", "Validation failed", HttpStatus.BAD_REQUEST),
    STUDENT_NOT_FOUND("E002", "Student not found", HttpStatus.NOT_FOUND),
    USERNAME_EXISTS("E003", "Username already exists", HttpStatus.CONFLICT),
    EMAIL_EXISTS("E004", "Email already exists", HttpStatus.CONFLICT),
    INTERNAL_SERVER_ERROR("E500", "Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus status;

    ErrorCode(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}
