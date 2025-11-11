package com.techcode.SpringSecurityApp.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techcode.SpringSecurityApp.dto.requestdto.StudentRequest;
import com.techcode.SpringSecurityApp.dto.rsponsedto.StudentResponse;
import com.techcode.SpringSecurityApp.service.StudentService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

@Validated
@RestController
@RequestMapping("/api/students")
@Slf4j
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }
   
    // Register new student
    @PostMapping("/register")
    public StudentResponse registerStudent(@Valid @RequestBody StudentRequest request) {
        log.info("Controller Layer::Incoming request for registration method: {}", request.getUsername());
        return studentService.registerStudent(request);
    }

    // Get all students
    @GetMapping
    public List<StudentResponse> getAllStudents() {
        log.info("controller Layer::Incoming Request for getAllStudents methods to Fetching all students");
        return studentService.getAllStudents();
    }

    // Get student by username
    @GetMapping("/{username}")
    public StudentResponse getStudentByUsername(@PathVariable @NotNull(message = "Student ID must not be null")String username) {
        log.info("Controller Layer:: Incoming Reqeust for getStudetByUsername method: {}", username);
        return studentService.getStudentByUsername(username);
    }
    // DELETE
    @DeleteMapping("/{rollNumber}")
    public ResponseEntity<String> deleteStudent(@PathVariable @NotNull(message = "Student ID must not be null")String rollNumber) {
        studentService.deleteStudentByRollNumber(rollNumber);
        return ResponseEntity.ok("Student deleted successfully");
    }

    // UPDATE
    @PutMapping("/{rollNumber}")
    public ResponseEntity<StudentResponse> updateStudent(
            @PathVariable @NotNull(message = "Student ID must not be null")String rollNumber,
            @RequestBody @Valid StudentRequest request) {
        StudentResponse response = studentService.updateStudentByRollNumber(rollNumber, request);
        return ResponseEntity.ok(response);
    }

}
