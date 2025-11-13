package com.techcode.studentmgmt.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.techcode.studentmgmt.dto.requestdto.StudentRequest;
import com.techcode.studentmgmt.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Validated
@RestController
@RequestMapping("/api/students")
@Slf4j
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping("/register")
    public ResponseEntity<?> registerStudent(@RequestBody StudentRequest request) {
        log.info("StudentController::registerStudent called");
        return studentService.registerStudent(request);
    }

    @GetMapping
    public ResponseEntity<?> getAllStudents() {
        log.info("StudentController::getAllStudents called");
        return studentService.getAllStudents();
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<?> getStudentByUsername(@PathVariable String username) {
        log.info("StudentController::getStudentByUsername called");
        return studentService.getStudentByUsername(username);
    }

    @GetMapping("/roll/{rollNumber}")
    public ResponseEntity<?> getStudentByRollNumber(@PathVariable String rollNumber) {
        log.info("StudentController::getStudentByRollNumber called");
        return studentService.getStudentByRollNumber(rollNumber);
    }

    @DeleteMapping("/{rollNumber}")
    public ResponseEntity<?> deleteStudent(@PathVariable String rollNumber) {
        log.info("StudentController::deleteStudent called");
        return studentService.deleteStudentByRollNumber(rollNumber);
    }

    @PutMapping("/{rollNumber}")
    public ResponseEntity<?> updateStudent(
            @PathVariable String rollNumber,
            @RequestBody StudentRequest request) {
        log.info("StudentController::updateStudent called");
        return studentService.updateStudentByRollNumber(rollNumber, request);
    }
}
