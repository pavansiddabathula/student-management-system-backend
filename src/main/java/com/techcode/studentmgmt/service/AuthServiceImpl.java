package com.techcode.studentmgmt.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.techcode.studentmgmt.constants.ErrorCodeEnums;
import com.techcode.studentmgmt.constants.SuccessMessageConstants;
import com.techcode.studentmgmt.dto.requestdto.AdminLoginRequest;
import com.techcode.studentmgmt.dto.requestdto.ForgotPasswordRequest;
import com.techcode.studentmgmt.dto.requestdto.OtpVerifyRequest;
import com.techcode.studentmgmt.dto.requestdto.SetPasswordRequest;
import com.techcode.studentmgmt.dto.requestdto.StudentLoginRequest;
import com.techcode.studentmgmt.dto.responsedto.AuthResponse;
import com.techcode.studentmgmt.dto.responsedto.OtpSuccessResponse;
import com.techcode.studentmgmt.dto.responsedto.SuccessResponse;
import com.techcode.studentmgmt.entity.AdminInfo;
import com.techcode.studentmgmt.entity.StudentInfo;
import com.techcode.studentmgmt.exceptions.BusinessException;
import com.techcode.studentmgmt.exceptions.ValidationException;
import com.techcode.studentmgmt.repository.AdminRepository;
import com.techcode.studentmgmt.repository.StudentRepository;
import com.techcode.studentmgmt.secuirty.JwtUtil;
import com.techcode.studentmgmt.utils.EmailUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

	private final AdminRepository adminRepo;
	private final StudentRepository studentRepo;
	private final JwtUtil jwtUtil;
	private final PasswordEncoder encoder;
	private final OtpService otpService;
	private final EmailUtil emailUtil;

	// Admin login
	@Override
	public ResponseEntity<?> adminLogin(AdminLoginRequest request) {

		log.info("Admin login attempt for ID: {}", request.getAdminid());
		
		validateAdminLogin(request);

		AdminInfo admin = adminRepo.findByAdminId(request.getAdminid()).orElseThrow(() -> {
			log.error("Admin not found for ID: {}", request.getAdminid());
			return new BusinessException(ErrorCodeEnums.ADMIN_NOT_FOUND, request.getAdminid());
		});

		if (!encoder.matches(request.getPassword(), admin.getPassword())) {
			log.error("Invalid password for admin ID: {}", request.getAdminid());
			throw new BusinessException(ErrorCodeEnums.INVALID_ADMIN_PASSWORD, request.getAdminid());
		}

		log.info("Admin login successful for ID: {}", request.getAdminid());
		return ResponseEntity.ok(generateAdminToken(admin));
	}

	// Student login
	@Override
	public ResponseEntity<?> studentLogin(StudentLoginRequest request) {

		log.info("Student login attempt for roll: {}", request.getRollNumber());
		validateStudentLogin(request);
	

		StudentInfo student = studentRepo.findByRollNumber(request.getRollNumber()).orElseThrow(() -> {
			log.error("Student not found for roll number: {}", request.getRollNumber());
			return new BusinessException(ErrorCodeEnums.STUDENT_NOT_FOUND, request.getRollNumber());
		});

		if (!encoder.matches(request.getPassword(), student.getPassword())) {
			log.error("Invalid password for roll: {}", request.getRollNumber());
			throw new BusinessException(ErrorCodeEnums.INVALID_STUDENT_PASSWORD, request.getRollNumber());
		}

		log.info("Student login successful for roll: {}", request.getRollNumber());
		return ResponseEntity.ok(generateStudentToken(student));
		//return success(null,generateStudentToken(student),HttpStatus.CREATED);
	}

	// Generate Admin JWT
	private AuthResponse generateAdminToken(AdminInfo admin) {
		String token = jwtUtil.generateToken(admin.getAdminId(), Map.of("roles", List.of("ADMIN")));

		long expiry = jwtUtil.getExpiryInSeconds();

		return AuthResponse.builder().access_token(token).token_type("Bearer").expires_in(expiry)
				.issued_at(Instant.now()).roles(List.of("ADMIN")).user(Map.of("id", admin.getId(), "adminId",
						admin.getAdminId(), "name", admin.getName(), "email", admin.getEmail()))
				.build();
	}

	// Generate Student JWT
	private AuthResponse generateStudentToken(StudentInfo student) {
		String token = jwtUtil.generateToken(student.getRollNumber(), Map.of("roles", List.of("STUDENT")));

		long expiry = jwtUtil.getExpiryInSeconds();

		return AuthResponse.builder().access_token(token).token_type("Bearer").expires_in(expiry)
				.issued_at(Instant.now()).roles(List.of("STUDENT"))
				.user(Map.of("id", student.getId(), "email", student.getEmail(), "name", student.getFullName()))
				.build();
	}

	// Verify OTP
	@Override
	public ResponseEntity<?> verifyOtp(OtpVerifyRequest req) {
	     String identifier = req.getIdentifier();
		if (!otpService.validateOtp(identifier, req.getOtp())) {
			throw new BusinessException(ErrorCodeEnums.INVALID_OTP,identifier);
		}
		
		return ResponseEntity.status(HttpStatus.OK)
		        .body(
		            OtpSuccessResponse.builder()
		                .status("SUCCESS")
		                .identifier(identifier)
		                .message(SuccessMessageConstants.OTP_VERIFIED_SUCCESS.format(identifier))
		                .returnurl("http://localhost:3000/set-password")
		                .expiryTime(LocalDateTime.now().plusMinutes(5))
		                .timestamp(LocalDateTime.now())
		                .build());

	}

	// Set Password
	@Override
	public ResponseEntity<?> setPassword(SetPasswordRequest req) {
	    
     String identifier = req.getIdentifier();
	    log.info("AuthServiceImpl::setPassword {}", identifier);

	    if (!otpService.isOtpVerified(identifier)) {
	        throw new BusinessException(ErrorCodeEnums.OTP_NOT_VERIFIED, identifier);
	    }

	    String email = null;
	    String userName = null;    // fullName
	    String userId = null;      // rollNumber or adminId
	    String role = null;

	    boolean isAdmin = identifier.startsWith("ADM");

	    // ADMIN PASSWORD UPDATE
	    if (isAdmin) {

	        AdminInfo admin = adminRepo.findByAdminId(identifier)
	                .orElseThrow(() -> new BusinessException(ErrorCodeEnums.ADMIN_NOT_FOUND,identifier));

	        admin.setPassword(encoder.encode(req.getNewPassword()));
	        adminRepo.save(admin);

	        // Set mail details
	        email = admin.getEmail();
	        userName = admin.getName();
	        userId = admin.getAdminId();
	        role = "ADMIN";

	    } else {

	        // STUDENT PASSWORD UPDATE
	        StudentInfo student = studentRepo.findByRollNumber(identifier)
	                .orElseThrow(() -> new BusinessException(ErrorCodeEnums.STUDENT_NOT_FOUND,identifier));

	        student.setPassword(encoder.encode(req.getNewPassword()));
	        studentRepo.save(student);

	        // Set mail details
	        email = student.getEmail();
	        userName = student.getFullName();
	        userId = student.getRollNumber();
	        role = "STUDENT";
	    }

	    otpService.clearVerification(identifier);

	    // correct order: (email, fullName, userId, role)
	    emailUtil.sendPasswordChangeAlert(email, userName, userId, role);

	    return success(
	            SuccessMessageConstants.PASSWORD_UPDATE_SUCCESS.format(identifier),
	            null,
	            HttpStatus.ACCEPTED
	    );
	}


	// Forgot Password
	@Override
	public ResponseEntity<?> forgotPassword(ForgotPasswordRequest req) {

		
		String email;
		String fullName;

		boolean isAdmin = req.getIdentifier().startsWith("ADM");
		
		log.info("Processing forgot password for {}",req.getIdentifier());

		if (isAdmin) {
			AdminInfo admin = adminRepo.findByAdminId(req.getIdentifier())
					.orElseThrow(() -> new BusinessException(ErrorCodeEnums.ADMIN_NOT_FOUND,req.getIdentifier()));
			email = admin.getEmail();
			fullName = admin.getName();
		} else {
			StudentInfo student = studentRepo.findByRollNumber(req.getIdentifier())
					.orElseThrow(() -> new BusinessException(ErrorCodeEnums.STUDENT_NOT_FOUND, req.getIdentifier()));
			email = student.getEmail();
			fullName = student.getFullName();
		}
		

		String otp = otpService.generateOtp(req.getIdentifier());

		emailUtil.sendOtpMail(email, fullName, otp);

		return ResponseEntity.status(HttpStatus.OK)
		        .body(OtpSuccessResponse.builder()
		                .status("SUCCESS")
		                .identifier(req.getIdentifier())
		                .message(SuccessMessageConstants.OTP_SENT_SUCCESS.format(email))
		                .returnurl("http://localhost:3000/verify-otp")
		                .expiryTime(LocalDateTime.now().plusMinutes(5))
		                .timestamp(LocalDateTime.now())
		                .build());

	}

	//success
	private ResponseEntity<?> success(String message, Object data, HttpStatus status) {
		return ResponseEntity.status(status).body(SuccessResponse.builder().status("SUCCESS").message(message)
				.data(data).timestamp(LocalDateTime.now()).build());
	}

	private ResponseEntity<?> success(String message, Object data) {
		return success(message, data, HttpStatus.OK);
	}

	private ResponseEntity<?> success(String message, HttpStatus status) {
		return success(message, null, status);
	}
	
	public void validateAdminLogin(AdminLoginRequest request) {

	    Map<String, String> errors = new LinkedHashMap<>();

	    // Admin ID business rules
	    if (!request.getAdminid().matches("^[A-Za-z0-9]{6}$")) {
	        errors.put("adminid", "Admin ID must be exactly 6 alphanumeric characters");
	    }

	    // Password business rules
	    if (request.getPassword().trim().length() < 8) {
	        errors.put("password", "Password must be at least 8 characters long");
	    }

	    if (!errors.isEmpty()) {
	        throw new ValidationException(errors);
	    }
	}
	
	public void validateStudentLogin(StudentLoginRequest request) {

	    Map<String, String> errors = new LinkedHashMap<>();

	    // Roll Number must be 10 characters
	    if (request.getRollNumber() == null || request.getRollNumber().length() != 10) {
	        errors.put("rollNumber", "Roll Number must be exactly 10 characters");
	    }

	    // Password minimum 8 characters
	    if (request.getPassword() == null || request.getPassword().trim().length() < 8) {
	        errors.put("password", "Password must be at least 8 characters long");
	    }

	    if (!errors.isEmpty()) {
	        throw new ValidationException(errors);
	    }
	}


}
