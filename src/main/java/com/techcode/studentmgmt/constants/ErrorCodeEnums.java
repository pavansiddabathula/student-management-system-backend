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

    DATABASE_ERROR("E503", "Database is currently not available. Please try again.", HttpStatus.SERVICE_UNAVAILABLE),
    
	INVALID_ADMIN_PASSWORD("E006", "Invalid password for admin ID '%s'.", HttpStatus.UNAUTHORIZED),
	
	INVALID_STUDENT_PASSWORD("E007", "Invalid password for roll number '%s'.", HttpStatus.UNAUTHORIZED),
	
	INVALID_OTP("E006", "Invalid or expired OTP for identifier '%s'.", HttpStatus.BAD_REQUEST),

	OTP_NOT_GENERATED("E007", "OTP was not generated or has expired for identifier '%s'.", HttpStatus.BAD_REQUEST),

	EMAIL_MISMATCH("E008", "Email '%s' does not match registered email for identifier '%s'.", HttpStatus.BAD_REQUEST),

	PASSWORDS_DO_NOT_MATCH("E009", "New password and confirm password do not match.", HttpStatus.BAD_REQUEST),

	WEAK_PASSWORD("E010", "Password must meet required security conditions.", HttpStatus.BAD_REQUEST),
	
	OTP_NOT_VERIFIED("E011", "OTP not verified for identifier '%s'.", HttpStatus.BAD_REQUEST),
	
	SERVICE_UNAVAILABLE("E503", "Service is currently unavailable. Please try again later.", HttpStatus.SERVICE_UNAVAILABLE),

    JOB_NOT_FOUND("E003", "Job with id '%s' not found.", HttpStatus.NOT_FOUND),

    DUPLICATE_JOB("E004", "A similar job already exists.", HttpStatus.BAD_REQUEST),

    DUPLICATE_APPLICATION("E005", "You have already applied for this job.", HttpStatus.BAD_REQUEST),

    APPLICATION_NOT_FOUND("E006", "Application not found for id '%s'.", HttpStatus.NOT_FOUND),

	TRAINING_NOT_FOUND("E101", "Training with code '%s' not found.", HttpStatus.NOT_FOUND),

	DUPLICATE_TRAINING("E102", "Training with name '%s' already exists.", HttpStatus.BAD_REQUEST),

	INVALID_TRAINING_DATES("E103", "End date must be after start date.", HttpStatus.BAD_REQUEST),

	INVALID_TRAINING_PRICE("E104", "Training price cannot be negative.", HttpStatus.BAD_REQUEST),

	DUPLICATE_TRAINING_REGISTRATION("E105","Student '%s' is already registered for training '%s'.",HttpStatus.BAD_REQUEST),

	TRAINING_REGISTRATION_NOT_FOUND("E106", "No registrations found for training '%s'.", HttpStatus.NOT_FOUND);
	
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

