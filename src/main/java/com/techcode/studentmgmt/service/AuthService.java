package com.techcode.studentmgmt.service;

import org.springframework.http.ResponseEntity;

import com.techcode.studentmgmt.dto.requestdto.AuthRequest;
import com.techcode.studentmgmt.dto.responsedto.AuthResponse;

public interface AuthService {
    ResponseEntity<AuthResponse> adminLogin(AuthRequest request);
    ResponseEntity<AuthResponse> studentLogin(AuthRequest request);
}
