package com.techcode.studentmgmt.service;

import org.springframework.http.ResponseEntity;

import com.techcode.studentmgmt.dto.requestdto.StudentPasswordResetRequest;
import com.techcode.studentmgmt.dto.requestdto.StudentRequest;

public interface StudentService {

    ResponseEntity<?> registerStudent(StudentRequest request);

    ResponseEntity<?> getAllStudents();

    ResponseEntity<?> getStudentByRollNumber(String rollNumber);

    ResponseEntity<?> getStudentByName(String fullName);

    ResponseEntity<?> deleteStudentByRollNumber(String rollNumber);

    ResponseEntity<?> updateStudentByRollNumber(String rollNumber, StudentRequest request);

	ResponseEntity<?> resetPasswordByRollNumber(String rollNumber,StudentPasswordResetRequest request);

	ResponseEntity<?> getProfile(String identifier);
    
    
}
