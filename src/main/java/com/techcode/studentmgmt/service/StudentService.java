package com.techcode.studentmgmt.service;

import org.springframework.http.ResponseEntity;
import com.techcode.studentmgmt.dto.requestdto.StudentRequest;

public interface StudentService {

    ResponseEntity<?> registerStudent(StudentRequest request);

    ResponseEntity<?> getAllStudents();

    ResponseEntity<?> getStudentByRollNumber(String rollNumber);

    ResponseEntity<?> getStudentByUsername(String username);

    ResponseEntity<?> deleteStudentByRollNumber(String rollNumber);

    ResponseEntity<?> updateStudentByRollNumber(String rollNumber, StudentRequest request);
}
