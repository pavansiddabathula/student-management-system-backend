package com.techcode.studentmgmt.service;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private final java.util.concurrent.Executor emailExecutor = java.util.concurrent.Executors.newFixedThreadPool(5);
  

    // Register student
    @Override
    public ResponseEntity<?> registerStudent(StudentRequest request) {

        log.info("registerStudent called for {}", request.getRollNumber());

        validateStudent(request, null);

        String tempPassword = passwordGen.generatePassword(10);

        StudentInfo entity = StudentMapper.toEntity(request);
        entity.setPassword(encoder.encode(tempPassword));

        StudentInfo saved = studentRepository.save(entity);

        StudentResponse response = StudentMapper.toResponse(saved);

        emailUtil.sendPasswordMail(
                saved.getEmail(),
                saved.getFirstName() + " " + saved.getLastName(),
                saved.getRollNumber(),
                tempPassword,
                "STUDENT"
        );
        
        emailExecutor.execute(() ->{ 
            emailUtil.sendPasswordMail(
                saved.getEmail(),
                saved.getFirstName() + " " + saved.getLastName(),
                saved.getRollNumber(),
                tempPassword,
                "STUDENT"
            );}
        );

        return success(
                SuccessMessageConstants.STUDENT_REGISTER_SUCCESS.format(response.getRollNumber(),response.getEmail()),
                response,
                HttpStatus.CREATED
        );
    }

    // Get all students
    @Override
    //@Cacheable(value = "studentList")
    public ResponseEntity<?> getAllStudents() {

        log.info("Fetching all student records");

        List<StudentResponse> students = studentRepository.findAll()
                .stream()
                .map(StudentMapper::toResponse)
                .toList(); 

        log.info("Total students fetched = {}", students.size());

        if (students.isEmpty()) {
            return success(SuccessMessageConstants.STUDENTS_EMPTY.getMessage(), students);
        }

        return success(
                SuccessMessageConstants.STUDENTS_FETCH_SUCCESS.format(students.size()),
                students
        );
    }


    // Get by roll
    @Override
    @Cacheable(value = "studentByRoll", key = "#rollNumber")
    public ResponseEntity<?> getStudentByRollNumber(String rollNumber) {

       log.info("Fetching student by roll {}", rollNumber);

        StudentInfo student = studentRepository.findByRollNumber(rollNumber)
                .orElseThrow(() -> new BusinessException(ErrorCodeEnums.STUDENT_NOT_FOUND, rollNumber));

        return success(
                SuccessMessageConstants.STUDENT_FETCH_SUCCESS.format(rollNumber),
                StudentMapper.toResponse(student)
        );
    }

    // Get by name
   
    @Override
    public ResponseEntity<?> getStudentByName(String firstName) {

        log.info("Fetching student by name {}", firstName);

        StudentInfo student = studentRepository.findByfirstName(firstName)
                .orElseThrow(() -> new BusinessException(ErrorCodeEnums.STUDENT_NOY_FOUND_BYNAME, firstName));

        return success(
                SuccessMessageConstants.STUDENT_FETCH_SUCCESS.format(firstName),
                StudentMapper.toResponse(student)
        );
    }

    // Delete by roll
    @Override
    public ResponseEntity<?> deleteStudentByRollNumber(String rollNumber) {

        log.info("Deleting student with roll {}", rollNumber);

        StudentInfo student = studentRepository.findByRollNumber(rollNumber)
                .orElseThrow(() -> new BusinessException(ErrorCodeEnums.STUDENT_NOT_FOUND, rollNumber));

        studentRepository.delete(student);

        return success(
                SuccessMessageConstants.STUDENT_DELETE_SUCCESS.format(rollNumber),
                HttpStatus.CREATED
        );
    }

    // Update
    @Override
    public ResponseEntity<?> updateStudentByRollNumber(String rollNumber, StudentRequest request) {

        log.info("Updating student with roll {}", rollNumber);

        StudentInfo student = studentRepository.findByRollNumber(rollNumber)
                .orElseThrow(() -> new BusinessException(ErrorCodeEnums.STUDENT_NOT_FOUND, rollNumber));

        validateStudent(request, student.getId());

        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setEmail(request.getEmail());
        student.setBranch(request.getBranch());
        student.setPhoneNumber(request.getPhoneNumber());

        StudentInfo updated = studentRepository.save(student);

        return success(
                SuccessMessageConstants.STUDENT_UPDATE_SUCCESS.format(rollNumber),
                StudentMapper.toResponse(updated)
        );
    }

    // Reset Password
    @Override
    public ResponseEntity<?> resetPasswordByRollNumber(String rollNumber, StudentPasswordResetRequest req) {

        log.info("Resetting password for {}", rollNumber);

        StudentInfo student = studentRepository.findByRollNumber(rollNumber)
                .orElseThrow(() -> new BusinessException(ErrorCodeEnums.STUDENT_NOT_FOUND, rollNumber));
          
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
                student.getFirstName() + " " + student.getLastName(),
                student.getRollNumber(),
                "ChangedPassword(Hidden)",
                "PASSWORD-UPDATE"
        );

        return success(
                SuccessMessageConstants.STUDENT_PASSWORD_RESET.format(student.getRollNumber()),
                HttpStatus.ACCEPTED
        );
    }

    // Get Profile
    @Override
    public ResponseEntity<?> getProfile(String identifier) {

        log.info("Fetching profile for {}", identifier);

        StudentInfo student = studentRepository.findByRollNumber(identifier)
                .orElseThrow(() -> new BusinessException(ErrorCodeEnums.STUDENT_NOT_FOUND, identifier));

        return success(
                SuccessMessageConstants.STUDENT_FETCH_SUCCESS.format(identifier),
                StudentMapper.toResponse(student)
        );
    }


    // Success response builder
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

    private ResponseEntity<?> success(String message, HttpStatus status) {
        return success(message, null, status);
    }

    // Validation logic
    private void validateStudent(StudentRequest request, Long currentId) {

        Map<String, String> errors = new LinkedHashMap<>();

        validator.validate(request).forEach(v ->
                errors.put(v.getPropertyPath().toString(), v.getMessage())
        );

        studentRepository.findByEmail(request.getEmail())
                .filter(s -> !s.getId().equals(currentId))
                .ifPresent(s -> errors.put(ResponseKeys.EMAIL,
                        String.format("Email '%s' is already registered.", request.getEmail()))
                );
        studentRepository.findByRollNumber(request.getRollNumber())
                .filter(s -> !s.getId().equals(currentId))
                .ifPresent(s -> errors.put(ResponseKeys.ROLL_NUMBER,
                        String.format("Roll Number '%s' is already registered.", request.getRollNumber()))
                );
        if (!errors.isEmpty()) {
        	
           throw new ValidationException(errors);
        	
     
            
        }
    }

	
}
