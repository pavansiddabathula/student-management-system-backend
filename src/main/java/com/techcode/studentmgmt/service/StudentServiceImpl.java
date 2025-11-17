package com.techcode.studentmgmt.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.techcode.studentmgmt.constants.SuccessMessageConstants;
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
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final StudentValidationUtil validationUtil;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    /** REGISTER student */
    @Override
    public ResponseEntity<?> registerStudent(StudentRequest request) {
        log.info("StudentServiceImpl::registerStudent request: {}", request);

        validationUtil.validateAll(request, null);

        request.setPassword(encoder.encode(request.getPassword()));
        StudentInfo saved = studentRepository.save(StudentMapper.toEntity(request));
        StudentResponse response = StudentMapper.toResponse(saved);

        log.info("Student registered successfully, rollNumber={}", response.getRollNumber());

        return ResponseBuilder.success(
                String.format(SuccessMessageConstants.STUDENT_REGISTER_SUCCESS, response.getRollNumber()),
                response,
                HttpStatus.CREATED
        );
    }

    /** GET all students */
    @Override
    public ResponseEntity<?> getAllStudents() {
        log.info("StudentServiceImpl::getAllStudents called");

        List<StudentResponse> students = studentRepository.findAll()
                .stream()
                .map(StudentMapper::toResponse)
                .collect(Collectors.toList());

        log.info("Total student records found: {}", students.size());

        if (students.isEmpty()) {
            return ResponseBuilder.success(
                    SuccessMessageConstants.STUDENTS_EMPTY,
                    students,
                    HttpStatus.OK
            );
        }

        return ResponseBuilder.success(
                String.format(SuccessMessageConstants.STUDENTS_FETCH_SUCCESS, students.size()),
                students,
                HttpStatus.OK
        );
    }

    /** GET student by roll number */
    @Override
    public ResponseEntity<?> getStudentByRollNumber(String rollNumber) {
        log.info("Fetching student by rollNumber: {}", rollNumber);

        StudentInfo student = studentRepository.findByRollNumber(rollNumber)
                .orElseThrow(() -> {
                    log.warn("Student not found with rollNumber: {}", rollNumber);
                    return new BusinessException(ErrorCode.STUDENT_NOT_FOUND);
                });

        StudentResponse response = StudentMapper.toResponse(student);

        return ResponseBuilder.success(
                String.format(SuccessMessageConstants.STUDENT_FETCH_SUCCESS, rollNumber),
                response,
                HttpStatus.OK
        );
    }

    /** GET student by username */
    @Override
    public ResponseEntity<?> getStudentByUsername(String username) {
        log.info("Fetching student by username: {}", username);

        StudentInfo student = studentRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("Student not found with username: {}", username);
                    return new BusinessException(ErrorCode.STUDENT_NOT_FOUND);
                });

        StudentResponse response = StudentMapper.toResponse(student);

        return ResponseBuilder.success(
                "Student details fetched successfully for username " + username + ".",
                response,
                HttpStatus.OK
        );
    }

    /** DELETE student */
    @Override
    public ResponseEntity<?> deleteStudentByRollNumber(String rollNumber) {
        log.info("Deleting student with rollNumber: {}", rollNumber);

        StudentInfo student = studentRepository.findByRollNumber(rollNumber)
                .orElseThrow(() -> {
                    log.warn("Student not found for delete, rollNumber: {}", rollNumber);
                    return new BusinessException(ErrorCode.STUDENT_NOT_FOUND);
                });

        studentRepository.delete(student);
        log.info("Student deleted: rollNumber={}", rollNumber);

        return ResponseBuilder.success(
                String.format(SuccessMessageConstants.STUDENT_DELETE_SUCCESS, rollNumber),
                null,
                HttpStatus.OK
        );
    }

    /** UPDATE student */
    @Override
    public ResponseEntity<?> updateStudentByRollNumber(String rollNumber, StudentRequest request) {
        log.info("Updating student with rollNumber: {}", rollNumber);

        StudentInfo student = studentRepository.findByRollNumber(rollNumber)
                .orElseThrow(() -> {
                    log.warn("Student not found for update, rollNumber: {}", rollNumber);
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

        log.info("Student updated successfully, rollNumber={}", rollNumber);

        return ResponseBuilder.success(
                String.format(SuccessMessageConstants.STUDENT_UPDATE_SUCCESS, rollNumber),
                response,
                HttpStatus.OK
        );
    }
}
