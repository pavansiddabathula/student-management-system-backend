package com.techcode.studentmgmt.constants;

import lombok.Getter;

@Getter
public enum SuccessMessageConstants {

    STUDENT_REGISTER_SUCCESS("Student registered successfully with roll number %s."),
    STUDENT_FETCH_SUCCESS("Student details fetched successfully for roll number %s."),
    STUDENTS_FETCH_SUCCESS("Total %d student record(s) fetched successfully."),
    STUDENTS_EMPTY("No student records found."),
    STUDENT_UPDATE_SUCCESS("Student details updated successfully for roll number %s."),
    STUDENT_DELETE_SUCCESS("Student with roll number %s deleted successfully."),
    STUDENT_PASSWORD_RESET("Password reset successfully for student roll number %s."),
    ADMIN_CREATED("Admin account created successfully with Admin ID %s."),
    ADMIN_FETCH_SUCCESS("Admin details fetched successfully for Admin ID %s."),
    ADMIN_LIST_FETCHED("Total %d admin record(s) fetched successfully."),
    ADMIN_UPDATE_SUCCESS("Admin details updated successfully for Admin ID %s."),
    ADMIN_DELETE_SUCCESS("Admin with Admin ID %s deleted successfully."),
    ADMIN_PASSWORD_RESET("Password reset successfully for Admin ID %s."),

    OTP_SENT_SUCCESS("OTP sent successfully to email %s."),
    OTP_VERIFIED_SUCCESS("OTP verified successfully for %s."),
    PASSWORD_UPDATE_SUCCESS("Password updated successfully for %s."),

    SUCCESS("SUCCESS");

    private final String message;

    SuccessMessageConstants(String message) {
        this.message = message;
    }

    public String format(Object... args) {
        return String.format(this.message, args);
    }
}

