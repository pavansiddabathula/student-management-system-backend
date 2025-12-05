package com.techcode.studentmgmt.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techcode.studentmgmt.dto.requestdto.AdminLoginRequest;
import com.techcode.studentmgmt.dto.requestdto.ForgotPasswordRequest;
import com.techcode.studentmgmt.dto.requestdto.OtpVerifyRequest;
import com.techcode.studentmgmt.dto.requestdto.SetPasswordRequest;
import com.techcode.studentmgmt.dto.requestdto.StudentLoginRequest;
import com.techcode.studentmgmt.service.AuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("api/auth")
@CrossOrigin(origins = "http://localhost:3000") 
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    
    /** Admin login endpoint */
    @PostMapping("/admin/login")
    public ResponseEntity<?> adminLogin(@RequestBody AdminLoginRequest request) {
        log.info("Admin login attempt: {}", request.getAdminid());
        return authService.adminLogin(request);
    }
    /*
    public String propString () {
    	 log.info(" alled this method after deploying in the aws");
    	return "hello brother ";
    }*/
    
    
    /** Student login endpoint */
    @PostMapping("/student/login")
    public ResponseEntity<?> studentLogin(@RequestBody StudentLoginRequest request) {
        log.info("Student login attempt: {}", request.getRollNumber());
        return authService.studentLogin(request);
    }
    
    /** Forgot Password endpoint */
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        
        return authService.forgotPassword(request);
    }
  
    /** Verify OTP endpoint */
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody OtpVerifyRequest request) {
    	
    	 String identifier = request.getIdentifier();
    	 log.info("authcontroller :: {}",identifier );
        return authService.verifyOtp(request);
    }

    /** Set New Password endpoint */
    @PostMapping("/set-password")
    public ResponseEntity<?> setPassword(@RequestBody SetPasswordRequest request) {
    	 log.info("authcontroller :: {}",request.getIdentifier() );
        return authService.setPassword(request);
    }
}