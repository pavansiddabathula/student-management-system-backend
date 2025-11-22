package com.techcode.studentmgmt.service;

import org.springframework.http.ResponseEntity;

import com.techcode.studentmgmt.dto.requestdto.AdminLoginRequest;
import com.techcode.studentmgmt.dto.requestdto.AuthRequest;
import com.techcode.studentmgmt.dto.responsedto.AuthResponse;

public interface AuthService {
    ResponseEntity<?> adminLogin(AdminLoginRequest request);
    ResponseEntity<AuthResponse> studentLogin(AuthRequest request);
}
