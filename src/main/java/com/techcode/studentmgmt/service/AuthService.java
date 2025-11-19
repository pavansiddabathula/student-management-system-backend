package com.techcode.studentmgmt.service;

import com.techcode.studentmgmt.dto.requestdto.AuthRequest;
import com.techcode.studentmgmt.dto.responsedto.AuthResponse;

import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<AuthResponse> adminLogin(AuthRequest request);
    ResponseEntity<AuthResponse> studentLogin(AuthRequest request);
}
