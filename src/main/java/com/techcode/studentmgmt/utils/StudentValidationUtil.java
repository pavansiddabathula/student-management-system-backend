package com.techcode.studentmgmt.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.techcode.studentmgmt.dto.requestdto.StudentRequest;
import com.techcode.studentmgmt.repository.StudentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Component
@Slf4j
@RequiredArgsConstructor
public class StudentValidationUtil {

    private final StudentRepository studentRepository;

    /**
     * Validate business rules for student registration or update.
     *
     * @param request StudentRequest object
     * @param currentStudentId For update: id of existing student, null for insert
     * @return Map of field -> error message
     */
    public Map<String, String> validateBusinessRules(StudentRequest request, Long currentStudentId) {
        Map<String, String> errors = new HashMap<>();

        // --- Password match check ---
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            errors.put("password", "Password and Confirm Password do not match");
        }

        // --- Username uniqueness ---
        studentRepository.findByUsername(request.getUsername())
                .filter(s -> currentStudentId == null || !s.getId().equals(currentStudentId))
                .ifPresent(s -> errors.put("username", "Username already exists"));

        // --- Email uniqueness ---
        studentRepository.findByEmail(request.getEmail())
                .filter(s -> currentStudentId == null || !s.getId().equals(currentStudentId))
                .ifPresent(s -> errors.put("email", "Email already exists"));

        return errors;
    }
}
