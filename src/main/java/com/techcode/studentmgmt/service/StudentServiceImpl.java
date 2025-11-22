package com.techcode.studentmgmt.service;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.techcode.studentmgmt.constants.ErrorCodeEnums;
import com.techcode.studentmgmt.constants.ResponseKeys;
import com.techcode.studentmgmt.constants.SuccessMessageConstants;
import com.techcode.studentmgmt.dto.requestdto.StudentPasswordResetRequest;
import com.techcode.studentmgmt.dto.requestdto.StudentRequest;
import com.techcode.studentmgmt.dto.responsedto.StudentResponse;
import com.techcode.studentmgmt.dto.responsedto.SuccessResponse;
import com.techcode.studentmgmt.entity.StudentInfo;
import com.techcode.studentmgmt.exceptions.BusinessException;
import com.techcode.studentmgmt.exceptions.ValidationException;
import com.techcode.studentmgmt.modelmappers.StudentMapper;
import com.techcode.studentmgmt.repository.StudentRepository;
import com.techcode.studentmgmt.utils.EmailUtil;
import com.techcode.studentmgmt.utils.PasswordGeneratorUtil;

import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final Validator validator;
    private final PasswordGeneratorUtil passwordGen;
    private final EmailUtil emailUtil;
    private final PasswordEncoder encoder;


    /* REGISTER STUDENT (Admin creates student) */
    @Override
    public ResponseEntity<?> registerStudent(StudentRequest request) {

        log.info("StudentServiceImpl::registerStudent called for {}", request.getRollNumber());

        // Validate
        validateStudent(request, null);

        // Generate random password
        String tempPassword = passwordGen.generatePassword(10);

        // Convert DTO → Entity
        StudentInfo entity = StudentMapper.toEntity(request);
        entity.setPassword(encoder.encode(tempPassword));

        // Save student
        StudentInfo saved = studentRepository.save(entity);

        // Convert back to response DTO
        StudentResponse response = StudentMapper.toResponse(saved);

        // Email notification
        
        emailUtil.sendPasswordMail(
                saved.getEmail(),
                saved.getFullName(),
                saved.getRollNumber(),
                tempPassword,
                "STUDENT"
        );

        return success(
                String.format(SuccessMessageConstants.STUDENT_REGISTER_SUCCESS, response.getRollNumber()),
                response,
                HttpStatus.CREATED
        );
    }

    /* GET ALL STUDENTS */
    @Override
    @Cacheable(value = "studentList")
    public ResponseEntity<?> getAllStudents() {

        List<StudentResponse> students = studentRepository.findAll()
                .stream()
                .map(StudentMapper::toResponse)
                .collect(Collectors.toList());

        if (students.isEmpty()) {
            return success(SuccessMessageConstants.STUDENTS_EMPTY, students);
        }

        return success(
                String.format(SuccessMessageConstants.STUDENTS_FETCH_SUCCESS, students.size()),
                students
        );
    }

    /* GET BY ROLL NUMBER */
    @Override
    @Cacheable(value = "studentByRoll", key = "#rollNumber")
    public ResponseEntity<?> getStudentByRollNumber(String rollNumber) {

        log.info("Fetching student by roll number {}", rollNumber);

        StudentInfo student = studentRepository.findByRollNumber(rollNumber)
                .orElseThrow(() ->
                        new BusinessException(ErrorCodeEnums.STUDENT_NOT_FOUND, rollNumber)
                );

        return success(
                String.format(SuccessMessageConstants.STUDENT_FETCH_SUCCESS, rollNumber),
                StudentMapper.toResponse(student)
        );
    }

    /* GET BY USERNAME */
    @Override
    public ResponseEntity<?> getStudentByName(String fullName) {

        log.info("Fetching student by username {}", fullName);

        StudentInfo student = studentRepository.findByfullName(fullName)
                .orElseThrow(() ->
                        new BusinessException(ErrorCodeEnums.STUDENT_NOY_FOUND_BYNAME, fullName)
                );

        return success(
                "Student details fetched successfully for username " + fullName,
                StudentMapper.toResponse(student)
        );
    }

    /* DELETE STUDENT */
    @Override
    public ResponseEntity<?> deleteStudentByRollNumber(String rollNumber) {

        log.info("Deleting student with roll number {}", rollNumber);

        StudentInfo student = studentRepository.findByRollNumber(rollNumber)
                .orElseThrow(() ->
                        new BusinessException(ErrorCodeEnums.STUDENT_NOT_FOUND, rollNumber)
                );

        studentRepository.delete(student);

        return success(
                String.format(SuccessMessageConstants.STUDENT_DELETE_SUCCESS, rollNumber),
                null
        );
    }

    /* UPDATE STUDENT (Admin updates student record) */
    @Override
    public ResponseEntity<?> updateStudentByRollNumber(String rollNumber, StudentRequest request) {

        log.info("Updating student with roll number {}", rollNumber);

        StudentInfo student = studentRepository.findByRollNumber(rollNumber)
                .orElseThrow(() ->
                        new BusinessException(ErrorCodeEnums.STUDENT_NOT_FOUND, rollNumber)
                );

        validateStudent(request, student.getId());

  
        student.setFullName(request.getFullName());
        student.setEmail(request.getEmail());
        student.setBranch(request.getBranch());
        student.setPhoneNumber(request.getPhoneNumber());

        StudentInfo updated = studentRepository.save(student);

        return success(
                String.format(SuccessMessageConstants.STUDENT_UPDATE_SUCCESS, rollNumber),
                StudentMapper.toResponse(updated)
        );
    }

    /* SUCCESS RESPONSE */
    private ResponseEntity<?> success(String message, Object data, HttpStatus status) {
        return ResponseEntity.status(status).body(
                SuccessResponse.builder()
                        .status("SUCCESS")
                        .message(message)
                        .data(data)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    private ResponseEntity<?> success(String message, Object data) {
        return success(message, data, HttpStatus.OK);
    }

    /* VALIDATION LOGIC */
    private void validateStudent(StudentRequest request, Long currentStudentId) {

        Map<String, String> errors = new LinkedHashMap<>();

        validator.validate(request).forEach(v ->
                errors.put(v.getPropertyPath().toString(), v.getMessage())
        );


        studentRepository.findByEmail(request.getEmail())
                .filter(s -> !s.getId().equals(currentStudentId))
                .ifPresent(s ->
                        errors.put(ResponseKeys.EMAIL,
                            String.format("Email '%s' is already registered.", request.getEmail()))
                );

        studentRepository.findByRollNumber(request.getRollNumber())
                .filter(s -> !s.getId().equals(currentStudentId))
                .ifPresent(s ->
                        errors.put(ResponseKeys.ROLL_NUMBER,
                              String.format("Roll Number '%s' is already registered.", request.getRollNumber()))
                );

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    @Override
    public ResponseEntity<?> resetPasswordByRollNumber(String rollNumber, StudentPasswordResetRequest req) {

        log.info("StudentServiceImpl::resetPasswordByRollNumber {}", rollNumber);

        StudentInfo student = studentRepository.findByRollNumber(rollNumber)
                .orElseThrow(() -> new BusinessException(
                        ErrorCodeEnums.STUDENT_NOT_FOUND, rollNumber));

        Map<String, String> errors = new LinkedHashMap<>();

        if (!encoder.matches(req.getOldPassword(), student.getPassword())) {
            errors.put("oldPassword", "Old password is incorrect.");
        }

        if (!req.getNewPassword().equals(req.getConfirmPassword())) {
            errors.put("confirmPassword", "New password and confirm password do not match.");
        }

        if (req.getOldPassword().equals(req.getNewPassword())) {
            errors.put("newPassword", "New password cannot be the same as old password.");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        student.setPassword(encoder.encode(req.getNewPassword()));
        studentRepository.save(student);

        emailUtil.sendPasswordMail(
                student.getEmail(),
                student.getFullName(),
                student.getRollNumber(),
                "ChangedPassword(Hidden)",
                "PASSWORD-UPDATE"
        );

        return success(String.format(SuccessMessageConstants.STUDENT_PASSWORD_RESET,student.getRollNumber()),HttpStatus.ACCEPTED
        );
    }

}
