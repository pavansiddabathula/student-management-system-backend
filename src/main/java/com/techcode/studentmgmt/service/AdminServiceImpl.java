package com.techcode.studentmgmt.service;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.techcode.studentmgmt.constants.ErrorCodeEnums;
import com.techcode.studentmgmt.constants.SuccessMessageConstants;
import com.techcode.studentmgmt.dto.requestdto.AdminPasswordResetRequest;
import com.techcode.studentmgmt.dto.requestdto.AdminRequest;
import com.techcode.studentmgmt.dto.responsedto.AdminResponse;
import com.techcode.studentmgmt.dto.responsedto.SuccessResponse;
import com.techcode.studentmgmt.entity.AdminInfo;
import com.techcode.studentmgmt.exceptions.BusinessException;
import com.techcode.studentmgmt.exceptions.ValidationException;
import com.techcode.studentmgmt.modelmappers.AdminMapper;
import com.techcode.studentmgmt.repository.AdminRepository;
import com.techcode.studentmgmt.utils.AdminIdGenerator;
import com.techcode.studentmgmt.utils.EmailUtil;
import com.techcode.studentmgmt.utils.PasswordGeneratorUtil;

import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepo;
    private final PasswordGeneratorUtil passwordGen;
    private final AdminIdGenerator adminIdGen;
    private final EmailUtil emailUtil;
    private final Validator validator;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // Create new admin
    @Override
    public ResponseEntity<?> createAdmin(AdminRequest request) {

        log.info("AdminServiceImpl::createAdmin {}", request.getEmail());

        validateAdmin(request, null);

        String lastAdminId = adminRepo.findLatestAdminId();
        String adminId = adminIdGen.generateAdminId(lastAdminId);
        String tempPwd = passwordGen.generatePassword(10);

        AdminInfo admin = AdminMapper.toEntity(request);
        admin.setAdminId(adminId);
        admin.setPassword(encoder.encode(tempPwd));

        adminRepo.save(admin);

        emailUtil.sendPasswordMail(
                admin.getEmail(),
                admin.getName(),
                admin.getAdminId(),
                tempPwd,
                "ADMIN"
        );

        return buildSuccess(
                SuccessMessageConstants.ADMIN_CREATED.format(adminId),
                AdminMapper.toResponse(admin),
                HttpStatus.CREATED
        );
    }

    // Get all admins
    @Override
    public ResponseEntity<?> getAllAdmins() {

        log.info("AdminServiceImpl::getAllAdmins");

        List<AdminResponse> admins = adminRepo.findAll()
                .stream()
                .map(AdminMapper::toResponse)
                .toList();

        return buildSuccess(
                SuccessMessageConstants.ADMIN_LIST_FETCHED.format(admins.size()),
                admins,
                HttpStatus.OK
        );
    }

    // Get admin by adminId
    @Override
    public ResponseEntity<?> getAdminById(String adminId) {

        log.info("AdminServiceImpl::getAdminById {}", adminId);

        AdminInfo admin = adminRepo.findByAdminId(adminId)
                .orElseThrow(() -> new BusinessException(ErrorCodeEnums.ADMIN_NOT_FOUND, adminId));

        return buildSuccess(
                SuccessMessageConstants.ADMIN_FETCH_SUCCESS.format(adminId),
                AdminMapper.toResponse(admin),
                HttpStatus.OK
        );
    }

    // Get admin by name
    @Override
    public ResponseEntity<?> getAdminByName(String name) {

        log.info("AdminServiceImpl::getAdminByName {}", name);

        List<AdminInfo> admins = adminRepo.findByNameIgnoreCase(name);

        if (admins.isEmpty()) {
            throw new BusinessException(ErrorCodeEnums.ADMIN_NOT_FOUND, name);
        }

        List<AdminResponse> list = admins.stream()
                .map(AdminMapper::toResponse)
                .toList();

        return buildSuccess(
                SuccessMessageConstants.ADMIN_LIST_FETCHED.format(list.size()),
                list
        );
    }

    // Update admin
    @Override
    public ResponseEntity<?> updateAdminById(String adminId, AdminRequest request) {

        log.info("AdminServiceImpl::updateAdminById {}", adminId);

        AdminInfo admin = adminRepo.findByAdminId(adminId)
                .orElseThrow(() -> new BusinessException(ErrorCodeEnums.ADMIN_NOT_FOUND, adminId));

        validateAdmin(request, admin.getId());

        admin.setName(request.getName());
        admin.setEmail(request.getEmail());
        admin.setPhoneNumber(request.getPhoneNumber());

        AdminInfo updated = adminRepo.save(admin);

        return buildSuccess(
                SuccessMessageConstants.ADMIN_UPDATE_SUCCESS.format(adminId),
                AdminMapper.toResponse(updated)
        );
    }

    // Delete admin
    @Override
    public ResponseEntity<?> deleteAdminById(String adminId) {

        log.info("AdminServiceImpl::deleteAdminById {}", adminId);

        AdminInfo admin = adminRepo.findByAdminId(adminId)
                .orElseThrow(() -> new BusinessException(ErrorCodeEnums.ADMIN_NOT_FOUND, adminId));

        adminRepo.delete(admin);

        return buildSuccess(
                SuccessMessageConstants.ADMIN_DELETE_SUCCESS.format(adminId),
                HttpStatus.OK
        );
    }

    // Reset password
    @Override
    public ResponseEntity<?> resetAdminPassword(String adminId, AdminPasswordResetRequest req) {

        log.info("AdminServiceImpl::resetAdminPassword {}", adminId);

        AdminInfo admin = adminRepo.findByAdminId(adminId)
                .orElseThrow(() -> new BusinessException(ErrorCodeEnums.ADMIN_NOT_FOUND, adminId));

        Map<String, String> errors = new LinkedHashMap<>();

        if (!encoder.matches(req.getOldPassword(), admin.getPassword())) {
            errors.put("oldPassword", "Old password is incorrect.");
        }

        if (!req.getNewPassword().equals(req.getConfirmPassword())) {
            errors.put("confirmPassword", "New password and confirm password do not match.");
        }

        if (req.getOldPassword().equals(req.getNewPassword())) {
            errors.put("newPassword", "New password cannot be the same as old password.");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        admin.setPassword(encoder.encode(req.getNewPassword()));
        adminRepo.save(admin);

        emailUtil.sendPasswordMail(
                admin.getEmail(),
                admin.getName(),
                admin.getAdminId(),
                "ChangedPassword(Hidden)",
                "ADMIN-PASSWORD-UPDATE"
        );

        return buildSuccess(
                SuccessMessageConstants.ADMIN_PASSWORD_RESET.format(adminId),
                HttpStatus.OK
        );
    }

    // Success wrapper
    private ResponseEntity<?> buildSuccess(String message, Object data, HttpStatus status) {
        return ResponseEntity.status(status)
                .body(SuccessResponse.builder()
                        .status("SUCCESS")
                        .message(message)
                        .data(data)
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    private ResponseEntity<?> buildSuccess(String message, Object data) {
        return buildSuccess(message, data, HttpStatus.OK);
    }

    private ResponseEntity<?> buildSuccess(String message, HttpStatus status) {
        return buildSuccess(message, null, status);
    }

    // Validation
    private void validateAdmin(AdminRequest req, Long currentAdminId) {

        Map<String, String> errors = new LinkedHashMap<>();

        validator.validate(req).forEach(v ->
                errors.put(v.getPropertyPath().toString(), v.getMessage())
        );

        adminRepo.findByEmail(req.getEmail())
                .filter(a -> !a.getId().equals(currentAdminId))
                .ifPresent(a -> errors.put("email",
                        String.format("Email '%s' already exists.", req.getEmail())));

        adminRepo.findByPhoneNumber(req.getPhoneNumber())
                .filter(a -> !a.getId().equals(currentAdminId))
                .ifPresent(a -> errors.put("phoneNumber",
                        String.format("Phone number '%s' already exists.", req.getPhoneNumber())));

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}
