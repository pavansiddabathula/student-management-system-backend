package com.techcode.studentmgmt.service;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.techcode.studentmgmt.constants.ErrorCodeEnums;
import com.techcode.studentmgmt.constants.ResponseKeys;
import com.techcode.studentmgmt.constants.SuccessMessageConstants;
import com.techcode.studentmgmt.dto.requestdto.StudentRequest;
import com.techcode.studentmgmt.dto.responsedto.StudentResponse;
import com.techcode.studentmgmt.dto.responsedto.SuccessResponse;
import com.techcode.studentmgmt.entity.StudentInfo;
import com.techcode.studentmgmt.exceptions.ValidationException;
import com.techcode.studentmgmt.exceptions.BusinessException;
import com.techcode.studentmgmt.modelmappers.StudentMapper;
import com.techcode.studentmgmt.repository.StudentRepository;

import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentServiceImpl implements StudentService {
	
	    private final StudentRepository studentRepository;
	    private final Validator validator;
	    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	
	    /* REGISTER STUDENT */
	    @Override
	    public ResponseEntity<?> registerStudent(StudentRequest request) {
	
	        validateStudent(request, null);
	        request.setPassword(encoder.encode(request.getPassword()));
	
	        StudentInfo saved = studentRepository.save(StudentMapper.toEntity(request));
	     
	        StudentResponse response = StudentMapper.toResponse(saved);
	
	        return success(
	                String.format(SuccessMessageConstants.STUDENT_REGISTER_SUCCESS, response.getRollNumber()),
	                response,
	                HttpStatus.CREATED
	        );
	    }
	
	    /* GET ALL STUDENTS */
	    @Override
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
	    public ResponseEntity<?> getStudentByRollNumber(String rollNumber) {
	
	        StudentInfo student = studentRepository.findByRollNumber(rollNumber)
	                .orElseThrow(() ->
	                        new BusinessException( ErrorCodeEnums.STUDENT_NOT_FOUND,rollNumber));
	
	        StudentResponse response = StudentMapper.toResponse(student);
	
	        return success(
	                String.format(SuccessMessageConstants.STUDENT_FETCH_SUCCESS, rollNumber),
	                response
	        );
	    }
	
	    /* GET BY USERNAME */
	    @Override
	    public ResponseEntity<?> getStudentByUsername(String username) {
	
	        StudentInfo student = studentRepository.findByUsername(username)
	                .orElseThrow(() ->
	                        new BusinessException(
	                                ErrorCodeEnums.STUDENT_NOY_FOUND_BYNAME,username));
	
	        StudentResponse response = StudentMapper.toResponse(student);
	
	        return success(
	                "Student details fetched successfully for username " + username,
	                response
	        );
	    }
	
	    /* DELETE */
	    @Override
	    public ResponseEntity<?> deleteStudentByRollNumber(String rollNumber) {
	
	        StudentInfo student = studentRepository.findByRollNumber(rollNumber)
	                .orElseThrow(() ->
	                        new BusinessException(
	                                ErrorCodeEnums.STUDENT_NOT_FOUND,rollNumber));
	                        
	
	        studentRepository.delete(student);
	
	        return success(
	                String.format(SuccessMessageConstants.STUDENT_DELETE_SUCCESS, rollNumber),
	                null
	        );
	    }
	
	    /* UPDATE */
	    @Override
	    public ResponseEntity<?> updateStudentByRollNumber(String rollNumber, StudentRequest request) {
	
	        StudentInfo student = studentRepository.findByRollNumber(rollNumber)
	                .orElseThrow(() ->
	                        new BusinessException(
	                                ErrorCodeEnums.STUDENT_NOT_FOUND,rollNumber));
	                   
	        validateStudent(request, student.getId());
	
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
	
	        return success(
	                String.format(SuccessMessageConstants.STUDENT_UPDATE_SUCCESS, rollNumber),
	                response
	        );
	    }
	
	
	    /* COMMON SUCCESS RESPONSE */
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
	
	    private ResponseEntity<?> success(String message) {
	        return success(message, null, HttpStatus.OK);
	    }
	
	
	    /* VALIDATION LOGIC */
	    private void validateStudent(StudentRequest request, Long currentStudentId) {
	
	        Map<String, String> errors = new LinkedHashMap<>();
	
	        validator.validate(request).forEach(v ->
	                errors.put(v.getPropertyPath().toString(), v.getMessage())
	        );
	
	        studentRepository.findByUsername(request.getUsername())
	                .filter(s -> !s.getId().equals(currentStudentId))
	                .ifPresent(s ->
	                        errors.put(ResponseKeys.USERNAME,
	                                "Username '" + request.getUsername() + "' is already exists.")
	                );
	
	        studentRepository.findByEmail(request.getEmail())
	                .filter(s -> !s.getId().equals(currentStudentId))
	                .ifPresent(s ->
	                        errors.put(ResponseKeys.EMAIL,
	                                "Email '" + request.getEmail() + "' is already exists.")
	                );
	
	        studentRepository.findByRollNumber(request.getRollNumber())
	                .filter(s -> !s.getId().equals(currentStudentId))
	                .ifPresent(s ->
	                        errors.put(ResponseKeys.ROLL_NUMBER,
	                                "Roll number '" + request.getRollNumber() + "' is already exists.")
	                );
	
	        if (request.getPassword() != null && request.getConfirmPassword() != null &&
	                !request.getPassword().equals(request.getConfirmPassword())) {
	
	            errors.put(ResponseKeys.CONFIRM_PASSWORD, "Password and Confirm Password do not match.");
	        }
	
	        if (!errors.isEmpty()) {
	            throw new ValidationException(errors);
	        }
	    }
}
