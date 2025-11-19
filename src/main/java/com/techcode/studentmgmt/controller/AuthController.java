package com.techcode.studentmgmt.controller;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techcode.studentmgmt.dto.requestdto.AuthRequest;
import com.techcode.studentmgmt.dto.responsedto.AuthResponse;
import com.techcode.studentmgmt.service.AuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    
    
    /** Admin login endpoint */
    @PostMapping("/admin/login")
    public ResponseEntity<AuthResponse> adminLogin(@RequestBody AuthRequest request) {
        log.info("Admin login attempt: {}", request.getRollNumber());
        return authService.adminLogin(request);
    }

    
    /** Student login endpoint */
    @PostMapping("/student/login")
    public ResponseEntity<AuthResponse> studentLogin(@RequestBody AuthRequest request) {
        log.info("Student login attempt: {}", request.getRollNumber());
        return authService.studentLogin(request);
    }
}