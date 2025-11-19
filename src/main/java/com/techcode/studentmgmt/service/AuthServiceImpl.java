package com.techcode.studentmgmt.service;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.techcode.studentmgmt.auth.AuthRequest;
import com.techcode.studentmgmt.auth.AuthResponse;
import com.techcode.studentmgmt.entity.AdminInfo;
import com.techcode.studentmgmt.entity.StudentInfo;
import com.techcode.studentmgmt.repository.AdminRepository;
import com.techcode.studentmgmt.repository.StudentRepository;
import com.techcode.studentmgmt.secuirty.JwtUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AdminRepository adminRepo;
    private final StudentRepository studentRepo;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    /* ADMIN LOGIN */
    @Override
    public ResponseEntity<AuthResponse> adminLogin(AuthRequest request) {

        AdminInfo admin = adminRepo.findByAdminId(request.getRollNumber())
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        if (!encoder.matches(request.getPassword(), admin.getPassword())) {
            throw new RuntimeException("Invalid admin password");
        }

        return ResponseEntity.ok(generateAdminToken(admin));
    }

    /* STUDENT LOGIN */
    @Override
    public ResponseEntity<AuthResponse> studentLogin(AuthRequest request) {

        StudentInfo student = studentRepo.findByRollNumber(request.getRollNumber())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        if (!encoder.matches(request.getPassword(), student.getPassword())) {
            throw new RuntimeException("Invalid student password");
        }

        return ResponseEntity.ok(generateStudentToken(student));
    }

    /* Generate Admin JWT */
    private AuthResponse generateAdminToken(AdminInfo admin) {
        String token = jwtUtil.generateToken(admin.getAdminId(),
                Map.of("roles", List.of("ADMIN")));

        long expiry = jwtUtil.getExpiryInSeconds();

        return AuthResponse.builder()
                .access_token(token)
                .token_type("Bearer")
                .expires_in(expiry)
                .issued_at(Instant.now())
                .roles(List.of("ADMIN"))
                .user(Map.of(
                        "id", admin.getId(),
                        "adminId", admin.getAdminId(),
                        "name", admin.getName(),
                        "email", admin.getEmail()
                ))
                .build();
    }

    /* Generate Student JWT */
    private AuthResponse generateStudentToken(StudentInfo student) {
        String token = jwtUtil.generateToken(student.getRollNumber(),
                Map.of("roles", List.of("STUDENT")));

        long expiry = jwtUtil.getExpiryInSeconds();

        return AuthResponse.builder()
                .access_token(token)
                .token_type("Bearer")
                .expires_in(expiry)
                .issued_at(Instant.now())
                .roles(List.of("STUDENT"))
                .user(Map.of(
                        "id", student.getId(),
            
                        "email", student.getEmail(),
                        "Name", student.getFullName()
                ))
                .build();
    }
}
