package com.techcode.studentmgmt.service;

import java.util.List;
import java.util.stream.Collectors;

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

    /** Register a new student */
    @Override
    public ResponseEntity<?> registerStudent(StudentRequest request) {
        log.info("StudentServiceImpl::registerStudent called with request: {}", request);

        validationUtil.validateAll(request, null);

        request.setPassword(encoder.encode(request.getPassword()));
        StudentInfo student = StudentMapper.toEntity(request);
        student = studentRepository.save(student);

        StudentResponse response = StudentMapper.toResponse(student);
        log.info("StudentServiceImpl::registerStudent success for studentId: {}", response.getId());

        return ResponseBuilder.success(SuccessMessageConstants.STUDENT_REGISTER_SUCCESS, response);
    }

    /** Get all students */
    @Override
    public ResponseEntity<?> getAllStudents() {
        log.info("StudentServiceImpl::getAllStudents called");

        List<StudentResponse> students = studentRepository.findAll()
                .stream()
                .map(StudentMapper::toResponse)
                .collect(Collectors.toList());

        log.info("StudentServiceImpl::getAllStudents completed. Count: {}", students.size());

        if (students.isEmpty()) {
            return ResponseBuilder.success(SuccessMessageConstants.STUDENTS_EMPTY, students);
        }

        return ResponseBuilder.success(SuccessMessageConstants.STUDENTS_FETCH_SUCCESS, students);
    }

    /** Get student by roll number */
    @Override
    public ResponseEntity<?> getStudentByRollNumber(String rollNumber) {
        log.info("StudentServiceImpl::getStudentByRollNumber called with rollNumber: {}", rollNumber);

        StudentInfo student = studentRepository.findByRollNumber(rollNumber)
                .orElseThrow(() -> {
                    log.warn("StudentServiceImpl::No student found with rollNumber: {}", rollNumber);
                    return new BusinessException(ErrorCode.STUDENT_NOT_FOUND);
                });

        StudentResponse response = StudentMapper.toResponse(student);
        log.info("StudentServiceImpl::getStudentByRollNumber success for rollNumber: {}", rollNumber);

        return ResponseBuilder.success(SuccessMessageConstants.STUDENT_FETCH_SUCCESS, response);
    }

    /** Get student by username */
    @Override
    public ResponseEntity<?> getStudentByUsername(String username) {
        log.info("StudentServiceImpl::getStudentByUsername called with username: {}", username);

        StudentInfo student = studentRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("StudentServiceImpl::No student found with username: {}", username);
                    return new BusinessException(ErrorCode.STUDENT_NOT_FOUND);
                });

        StudentResponse response = StudentMapper.toResponse(student);
        log.info("StudentServiceImpl::getStudentByUsername success for username: {}", username);

        return ResponseBuilder.success(SuccessMessageConstants.STUDENT_FETCH_SUCCESS, response);
    }

    /** Delete student */
    @Override
    public ResponseEntity<?> deleteStudentByRollNumber(String rollNumber) {
        log.info("StudentServiceImpl::deleteStudentByRollNumber called with rollNumber: {}", rollNumber);

        StudentInfo student = studentRepository.findByRollNumber(rollNumber)
                .orElseThrow(() -> {
                    log.warn("StudentServiceImpl::No student found for deletion with rollNumber: {}", rollNumber);
                    return new BusinessException(ErrorCode.STUDENT_NOT_FOUND);
                });

        studentRepository.delete(student);
        log.info("StudentServiceImpl::deleteStudentByRollNumber success for rollNumber: {}", rollNumber);

        return ResponseBuilder.success(SuccessMessageConstants.STUDENT_DELETE_SUCCESS);
    }

    /** Update student */
    @Override
    public ResponseEntity<?> updateStudentByRollNumber(String rollNumber, StudentRequest request) {
        log.info("StudentServiceImpl::updateStudentByRollNumber called with rollNumber: {}", rollNumber);

        StudentInfo student = studentRepository.findByRollNumber(rollNumber)
                .orElseThrow(() -> {
                    log.warn("StudentServiceImpl::No student found for update with rollNumber: {}", rollNumber);
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

        log.info("StudentServiceImpl::updateStudentByRollNumber success for rollNumber: {}", rollNumber);

        return ResponseBuilder.success(SuccessMessageConstants.STUDENT_UPDATE_SUCCESS, response);
    }
}
