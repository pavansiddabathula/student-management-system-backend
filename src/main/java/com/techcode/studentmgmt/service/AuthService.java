package com.techcode.studentmgmt.service;

import org.springframework.http.ResponseEntity;

import com.techcode.studentmgmt.dto.requestdto.AdminLoginRequest;
import com.techcode.studentmgmt.dto.requestdto.StudentLoginRequest;
import com.techcode.studentmgmt.dto.requestdto.ForgotPasswordRequest;
import com.techcode.studentmgmt.dto.requestdto.OtpVerifyRequest;
import com.techcode.studentmgmt.dto.requestdto.SetPasswordRequest;
import com.techcode.studentmgmt.dto.responsedto.AuthResponse;

public interface AuthService {
    ResponseEntity<?> adminLogin(AdminLoginRequest request);
    
    ResponseEntity<?> studentLogin(StudentLoginRequest request);
    
    ResponseEntity<?> forgotPassword(ForgotPasswordRequest request);

	ResponseEntity<?> verifyOtp(OtpVerifyRequest request);

	ResponseEntity<?> setPassword(SetPasswordRequest request);
}
