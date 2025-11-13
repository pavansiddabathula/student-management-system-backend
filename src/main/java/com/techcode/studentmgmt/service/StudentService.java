package com.techcode.studentmgmt.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.techcode.studentmgmt.dto.requestdto.StudentRequest;
import com.techcode.studentmgmt.dto.responsedto.StudentResponse;
import com.techcode.studentmgmt.entity.StudentInfo;
import com.techcode.studentmgmt.enums.ErrorCode;
import com.techcode.studentmgmt.exceptions.BusinessException;
import com.techcode.studentmgmt.modelmappers.StudentMapper;
import com.techcode.studentmgmt.repository.StudentRepository;
import com.techcode.studentmgmt.utils.ResponseBuilder;
import com.techcode.studentmgmt.utils.StudentValidationUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentValidationUtil validationUtil;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    /** Register a new student */
    public ResponseEntity<?> registerStudent(StudentRequest request) {
        log.info("StudentService::registerStudent called with request: {}", request);
        validationUtil.validateAll(request, null);

        request.setPassword(encoder.encode(request.getPassword()));
        StudentInfo student = StudentMapper.toEntity(request);
        student = studentRepository.save(student);

        StudentResponse response = StudentMapper.toResponse(student);
        log.info("StudentService::registerStudent success for studentId: {}", response.getId());
        return ResponseBuilder.success("Student registered successfully", response);
    }

    /** Get all students */
    public ResponseEntity<?> getAllStudents() {
        log.info("StudentService::getAllStudents called");
        List<StudentResponse> students = studentRepository.findAll()
                .stream()
                .map(StudentMapper::toResponse)
                .collect(Collectors.toList());
        log.info("StudentService::getAllStudents completed. Count: {}", students.size());
        return ResponseBuilder.success("All students fetched successfully", students);
    }

    /** Get student by roll number */
    public ResponseEntity<?> getStudentByRollNumber(String rollNumber) {
        log.info("StudentService::getStudentByRollNumber called with rollNumber: {}", rollNumber);
        StudentInfo student = studentRepository.findByRollNumber(rollNumber)
                .orElseThrow(() -> {
                    log.warn("StudentService::No student found with rollNumber: {}", rollNumber);
                    return new BusinessException(ErrorCode.STUDENT_NOT_FOUND);
                });
        StudentResponse response = StudentMapper.toResponse(student);
        log.info("StudentService::getStudentByRollNumber success for rollNumber: {}", rollNumber);
        return ResponseBuilder.success("Student fetched successfully", response);
    }

    /** Get student by username */
    public ResponseEntity<?> getStudentByUsername(String username) {
        log.info("StudentService::getStudentByUsername called with username: {}", username);
        StudentInfo student = studentRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("StudentService::No student found with username: {}", username);
                    return new BusinessException(ErrorCode.STUDENT_NOT_FOUND);
                });
        StudentResponse response = StudentMapper.toResponse(student);
        log.info("StudentService::getStudentByUsername success for username: {}", username);
        return ResponseBuilder.success("Student fetched successfully", response);
    }

    /** Delete student */
    public ResponseEntity<?> deleteStudentByRollNumber(String rollNumber) {
        log.info("StudentService::deleteStudentByRollNumber called with rollNumber: {}", rollNumber);
        StudentInfo student = studentRepository.findByRollNumber(rollNumber)
                .orElseThrow(() -> {
                    log.warn("StudentService::No student found for deletion with rollNumber: {}", rollNumber);
                    return new BusinessException(ErrorCode.STUDENT_NOT_FOUND);
                });
        studentRepository.delete(student);
        log.info("StudentService::deleteStudentByRollNumber success for rollNumber: {}", rollNumber);
        return ResponseBuilder.success("Student deleted successfully");
    }

    /** Update student */
    public ResponseEntity<?> updateStudentByRollNumber(String rollNumber, StudentRequest request) {
        log.info("StudentService::updateStudentByRollNumber called with rollNumber: {}", rollNumber);
        StudentInfo student = studentRepository.findByRollNumber(rollNumber)
                .orElseThrow(() -> {
                    log.warn("StudentService::No student found for update with rollNumber: {}", rollNumber);
                    return new BusinessException(ErrorCode.STUDENT_NOT_FOUND);
                });

        validationUtil.validateAll(request, student.getId());
        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setEmail(request.getEmail());
        student.setUsername(request.getUsername());
        student.setBranch(request.getBranch());
        student.setPhoneNumber(request.getPhoneNumber());

        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            student.setPassword(encoder.encode(request.getPassword()));
        }

        StudentInfo updated = studentRepository.save(student);
        StudentResponse response = StudentMapper.toResponse(updated);
        log.info("StudentService::updateStudentByRollNumber success for rollNumber: {}", rollNumber);
        return ResponseBuilder.success("Student updated successfully", response);
    }
}
