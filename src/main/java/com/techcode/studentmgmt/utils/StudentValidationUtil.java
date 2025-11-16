package com.techcode.studentmgmt.utils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.techcode.studentmgmt.constants.ErrorMessageConstants;
import com.techcode.studentmgmt.constants.ResponseKeys;
import com.techcode.studentmgmt.dto.requestdto.StudentRequest;
import com.techcode.studentmgmt.entity.StudentInfo;
import com.techcode.studentmgmt.exceptions.ValidationException;
import com.techcode.studentmgmt.repository.StudentRepository;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class StudentValidationUtil {

    private final Validator validator;
    private final StudentRepository studentRepository;

    /**
     * Validates both DTO (annotation) and business rules.
     *
     * @param request DTO to validate
     * @param currentStudentId for update scenarios, null for insert
     * @return map of field errors (optional)
     */
    public Map<String, String> validateAll(StudentRequest request, Long currentStudentId) {
        Map<String, String> errors = new LinkedHashMap<>();

        // 1️⃣ DTO validation
        Set<ConstraintViolation<StudentRequest>> violations = validator.validate(request);
        for (ConstraintViolation<StudentRequest> v : violations) {
            errors.put(v.getPropertyPath().toString(), v.getMessage());
            log.warn("DTO validation failed - Field: {}, Message: {}", v.getPropertyPath(), v.getMessage());
        }

        // 2️⃣ Business validation (duplicates)
        Optional<StudentInfo> existingByUsername = studentRepository.findByUsername(request.getUsername());
        if (existingByUsername.isPresent() && (currentStudentId == null || !existingByUsername.get().getId().equals(currentStudentId))) {
            errors.put(ResponseKeys.USERNAME,ErrorMessageConstants.USERNAME_ALREADY_EXISTS);
            log.warn("Business validation failed - username already exists: {}", request.getUsername());
        }

        Optional<StudentInfo> existingByEmail = studentRepository.findByEmail(request.getEmail());
        if (existingByEmail.isPresent() && (currentStudentId == null || !existingByEmail.get().getId().equals(currentStudentId))) {
            errors.put(ResponseKeys.EMAIL,ErrorMessageConstants.EMAIL_ALREADY_EXISTS);
            log.warn("Business validation failed - email already exists: {}", request.getEmail());
        }
        Optional<StudentInfo> existingByRollNumber = studentRepository.findByRollNumber(request.getRollNumber());
        if (existingByRollNumber.isPresent() && (currentStudentId == null || !existingByRollNumber.get().getId().equals(currentStudentId))) {
			errors.put(ResponseKeys.ROLL_NUMBER, ErrorMessageConstants.ROLLNUMBER_ALREADY_EXISTS);
			log.warn("Business validation failed - roll number already exists: {}", request.getRollNumber());
		}

        // 3️⃣ Password match check
        if (request.getPassword() != null && request.getConfirmPassword() != null &&
                !request.getPassword().equals(request.getConfirmPassword())) {
            errors.put(ResponseKeys.CONFIRM_PASSWORD, ErrorMessageConstants.PASSWORD_MISMATCH);
            log.warn("Password mismatch: password != confirmPassword");
        }

        // 4️⃣ Throw exception if any errors
        if (!errors.isEmpty()) {
            log.warn("Validation failed with errors: {}", errors);
            throw new ValidationException(errors);
        }

        log.info("Validation successful for student request: {}", request);
        return errors; // optional, can be removed if not needed
    }
}
