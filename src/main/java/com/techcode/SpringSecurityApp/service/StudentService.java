package com.techcode.SpringSecurityApp.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.techcode.SpringSecurityApp.dto.requestdto.StudentRequest;
import com.techcode.SpringSecurityApp.dto.rsponsedto.StudentResponse;
import com.techcode.SpringSecurityApp.entity.StudentInfo;
import com.techcode.SpringSecurityApp.modelmappers.StudentMapper;
import com.techcode.SpringSecurityApp.repository.StudentRepository;
import com.techcode.SpringSecurityApp.utils.StudentValidationUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentValidationUtil validationUtil;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    //Register a new student.
    public StudentResponse registerStudent(StudentRequest studentRequest) {
        log.info("StudentService :: Registering new student: {}", studentRequest.getUsername());

        //Validate request (insert → currentStudentId = null)
        Map<String, String> errors = validationUtil.validateBusinessRules(studentRequest, null);
        if (!errors.isEmpty()) {
            log.warn("Validation failed during registration: {}", errors);
            throw new IllegalArgumentException(errors.toString());
        }

        // Encode password
        studentRequest.setPassword(encoder.encode(studentRequest.getPassword()));

        //Convert DTO → Entity
        StudentInfo studentEntity = StudentMapper.toEntity(studentRequest);

        // Save to DB
        studentEntity = studentRepository.save(studentEntity);
        log.info("Student saved successfully: {}", studentEntity);

        //Convert Entity → Response DTO
        return StudentMapper.toResponse(studentEntity);
    }

    //Fetch a student by username.
    public StudentResponse getStudentByUsername(String username) {
        log.info("StudentService :: Fetching student by username: {}", username);
        StudentInfo student = studentRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return StudentMapper.toResponse(student);
    }

    //Get all students.
    public List<StudentResponse> getAllStudents() {
        log.info("StudentService :: Fetching all students...");
        return studentRepository.findAll()
                .stream()
                .map(StudentMapper::toResponse)
                .collect(Collectors.toList());
    }

    //Delete student by roll number.
     
    public void deleteStudentByRollNumber(String rollNumber) {
        log.info("Deleting student with rollNumber: {}", rollNumber);
        StudentInfo student = studentRepository.findByRollNumber(rollNumber)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        studentRepository.delete(student);
        log.info("Student deleted successfully: {}", rollNumber);
    }

    public StudentResponse updateStudentByRollNumber(String rollNumber, StudentRequest request) {
        log.info("Updating student with rollNumber: {} {}", rollNumber,request);

        String trimmedRollNumber = rollNumber.trim();
        StudentInfo student = studentRepository.findByRollNumber(trimmedRollNumber)
                .orElseThrow(() -> new RuntimeException("Student not found with rollNumber: " + trimmedRollNumber));

        // Validate request
        Map<String, String> errors = validationUtil.validateBusinessRules(request, student.getId());
        if (!errors.isEmpty()) {
            log.warn("Validation failed during update: {}", errors);
            throw new IllegalArgumentException(errors.toString());
        }

        // Update fields
        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setEmail(request.getEmail());
        student.setBranch(request.getBranch());
        student.setUsername(request.getUsername());
        student.setPhoneNumber(request.getPhoneNumber());

        // Update password only if provided
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            student.setPassword(encoder.encode(request.getPassword()));
        }

        StudentInfo updatedStudent = studentRepository.save(student);
        log.info("Student updated successfully: {}", updatedStudent);

        return StudentMapper.toResponse(updatedStudent);
    }
}
