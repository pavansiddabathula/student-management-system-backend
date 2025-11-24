package com.techcode.studentmgmt.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.techcode.studentmgmt.constants.ErrorCodeEnums;
import com.techcode.studentmgmt.constants.SuccessMessageConstants;
import com.techcode.studentmgmt.dto.requestdto.AdminLoginRequest;
import com.techcode.studentmgmt.dto.requestdto.AuthRequest;
import com.techcode.studentmgmt.dto.requestdto.ForgotPasswordRequest;
import com.techcode.studentmgmt.dto.requestdto.OtpVerifyRequest;
import com.techcode.studentmgmt.dto.requestdto.SetPasswordRequest;
import com.techcode.studentmgmt.dto.responsedto.AuthResponse;
import com.techcode.studentmgmt.dto.responsedto.OtpSuccessResponse;
import com.techcode.studentmgmt.dto.responsedto.SuccessResponse;
import com.techcode.studentmgmt.entity.AdminInfo;
import com.techcode.studentmgmt.entity.StudentInfo;
import com.techcode.studentmgmt.exceptions.BusinessException;
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
	public ResponseEntity<AuthResponse> studentLogin(AuthRequest request) {

		log.info("Student login attempt for roll: {}", request.getRollNumber());

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

		if (!otpService.validateOtp(req.getIdentifier(), req.getOtp())) {
			throw new BusinessException(ErrorCodeEnums.INVALID_OTP, req.getIdentifier());
		}
		return success(SuccessMessageConstants.OTP_VERIFIED_SUCCESS.format(req.getIdentifier()), null, HttpStatus.OK);
	}

	// Set Password
	@Override
	public ResponseEntity<?> setPassword(SetPasswordRequest req) {

	    log.info("AuthServiceImpl::setPassword {}", req.getIdentifier());

	    if (!otpService.isOtpVerified(req.getIdentifier())) {
	        throw new BusinessException(ErrorCodeEnums.OTP_NOT_VERIFIED, req.getIdentifier());
	    }

	    // validate password match logic here...

	    boolean isAdmin = req.getIdentifier().startsWith("ADM");

	    if (isAdmin) {
	        AdminInfo admin = adminRepo.findByAdminId(req.getIdentifier())
	                .orElseThrow(() -> new BusinessException(ErrorCodeEnums.ADMIN_NOT_FOUND, req.getIdentifier()));
	        admin.setPassword(encoder.encode(req.getNewPassword()));
	        adminRepo.save(admin);
	    } else {
	        StudentInfo student = studentRepo.findByRollNumber(req.getIdentifier())
	                .orElseThrow(() -> new BusinessException(ErrorCodeEnums.STUDENT_NOT_FOUND, req.getIdentifier()));
	        student.setPassword(encoder.encode(req.getNewPassword()));
	        studentRepo.save(student);
	    }

	    otpService.clearVerification(req.getIdentifier());  // remove verification flag

	    return success(
	            SuccessMessageConstants.PASSWORD_UPDATE_SUCCESS.format(req.getIdentifier()),
	            null,
	            HttpStatus.ACCEPTED
	    );
	}


	// Forgot Password
	@Override
	public ResponseEntity<?> forgotPassword(ForgotPasswordRequest req) {

		log.info("Processing forgot password for {}", req.getIdentifier());
		String email;
		String fullName;

		boolean isAdmin = req.getIdentifier().startsWith("ADM");

		if (isAdmin) {
			AdminInfo admin = adminRepo.findByAdminId(req.getIdentifier())
					.orElseThrow(() -> new BusinessException(ErrorCodeEnums.ADMIN_NOT_FOUND, req.getIdentifier()));
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
		                .expiryTime(LocalDateTime.now().plusMinutes(5))
		                .timestamp(LocalDateTime.now())
		                .build());

	}

	

	// Success response builder
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
}
