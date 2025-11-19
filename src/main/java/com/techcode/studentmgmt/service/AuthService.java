package com.techcode.studentmgmt.service;

import com.techcode.studentmgmt.auth.AuthRequest;
import com.techcode.studentmgmt.auth.AuthResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<AuthResponse> adminLogin(AuthRequest request);
    ResponseEntity<AuthResponse> studentLogin(AuthRequest request);
}
