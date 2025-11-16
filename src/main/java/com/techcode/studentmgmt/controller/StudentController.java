package com.techcode.studentmgmt.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.techcode.studentmgmt.dto.requestdto.StudentRequest;
import com.techcode.studentmgmt.service.StudentServiceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Validated
@RestController
@RequestMapping("/api/students")
@Slf4j
@RequiredArgsConstructor
public class StudentController {

	private final StudentServiceImpl studentServiceimpl;
 

    @PostMapping("/register")
    public ResponseEntity<?> registerStudent(@RequestBody StudentRequest request) {
        log.info("StudentController::registerStudent called");
        return studentServiceimpl.registerStudent(request);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllStudents() {
        log.info("StudentController::getAllStudents called");
        return studentServiceimpl.getAllStudents();
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<?> getStudentByUsername(@PathVariable String username) {
        log.info("StudentController::getStudentByUsername called");
        return studentServiceimpl.getStudentByUsername(username);
    }

    @GetMapping("/roll/{rollNumber}")
    public ResponseEntity<?> getStudentByRollNumber(@PathVariable String rollNumber) {
        log.info("StudentController::getStudentByRollNumber called");
        return studentServiceimpl.getStudentByRollNumber(rollNumber);
    }

    @DeleteMapping("/delete/{rollNumber}")
    public ResponseEntity<?> deleteStudent(@PathVariable String rollNumber) {
        log.info("StudentController::deleteStudent called");
        return studentServiceimpl.deleteStudentByRollNumber(rollNumber);
    }

    @PutMapping("/update/{rollNumber}")
    public ResponseEntity<?> updateStudent(
            @PathVariable String rollNumber,
            @RequestBody StudentRequest request) {
        log.info("StudentController::updateStudent called");
        return studentServiceimpl.updateStudentByRollNumber(rollNumber, request);
    }
}
