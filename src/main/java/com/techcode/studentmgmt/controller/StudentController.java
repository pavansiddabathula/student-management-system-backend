package com.techcode.studentmgmt.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techcode.studentmgmt.dto.requestdto.StudentPasswordResetRequest;
import com.techcode.studentmgmt.dto.requestdto.StudentRequest;
import com.techcode.studentmgmt.service.StudentService;
import com.techcode.studentmgmt.utils.EmailUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Validated
@RestController
@RequestMapping("/api/students")
@Slf4j
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    private final EmailUtil emailUtil;
  
    
    // Create a new student
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerStudent(@RequestBody StudentRequest request) {
        log.info("StudentController::registerStudent called");
        return studentService.registerStudent(request);
    }
    
    // Get all students (Only ADMIN)
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllStudents() {
        log.info("StudentController::getAllStudents called");
        return studentService.getAllStudents();
    }

    // Get student by full name (Only ADMIN)
    @GetMapping("/username/{fullName}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getStudentByUsername(@PathVariable String fullName) {
        log.info("StudentController::getStudentByUsername called");
        return studentService.getStudentByName(fullName);
    }

    // Get student by roll number
    @GetMapping("/roll/{rollNumber}")
    @PreAuthorize("hasAnyRole('ADMIN','STUDENT')")
    public ResponseEntity<?> getStudentByRollNumber(@PathVariable String rollNumber) {
        log.info("StudentController::getStudentByRollNumber called");
        return studentService.getStudentByRollNumber(rollNumber);
    }
    
    // Get own profile
    @GetMapping("/profile")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> getMyProfile() {

        String identifier = SecurityContextHolder.getContext().getAuthentication().getName();

        return studentService.getProfile(identifier);
    }

    // Delete student
    @DeleteMapping("/delete/{rollNumber}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteStudent(@PathVariable String rollNumber) {
        log.info("StudentController::deleteStudent called");
        return studentService.deleteStudentByRollNumber(rollNumber);
    }

    // Update student
    @PutMapping("/update/{rollNumber}")
    @PreAuthorize("hasAnyRole('STUDENT','ADMIN')")
    public ResponseEntity<?> updateStudent(
            @PathVariable String rollNumber,
            @RequestBody StudentRequest request) {
        log.info("StudentController::updateStudent called");
        return studentService.updateStudentByRollNumber(rollNumber, request);
    }

    // Student resets his own password
    @PutMapping("/reset-password")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> resetStudentPassword(
      
            @RequestBody StudentPasswordResetRequest request) {
        String rollNumber = SecurityContextHolder.getContext().getAuthentication().getName();
        //i want to print the  logs the content which is security context holder
        String securityContextInfo = SecurityContextHolder.getContext().getAuthentication().toString();
        log.info("Security Context Info: " + securityContextInfo);

        log.info("StudentController::resetStudentPassword called");
        return studentService.resetPasswordByRollNumber(rollNumber, request);
    }
    
    
    @GetMapping("/alll")
    public void mailtesting() {
        log.info("StudentController::getAllStudents called");
        //emailUtil.sendPasswordMail( "jayaramsiddabathula@gmail.com","Sulaga gopi kishore ","ADM004","q*c*%XM9s3","ADMIN");
        emailUtil.sendPasswordChangeAlert("pavansiddabathula@gmail.com","Saikumar", "21481A05K6", "STUDENT");
    }
}
