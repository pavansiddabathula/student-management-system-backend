package com.techcode.studentmgmt.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techcode.studentmgmt.dto.requestdto.AdminPasswordResetRequest;
import com.techcode.studentmgmt.dto.requestdto.AdminRequest;
import com.techcode.studentmgmt.service.AdminService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final AdminService adminService;

    /** Create admin */
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createAdmin(@RequestBody AdminRequest request) {
        log.info("AdminController::createAdmin {}", request.getEmail());
        return adminService.createAdmin(request);
    }

    /** Get all admins */
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllAdmins() {
        log.info("AdminController::getAllAdmins");
        return adminService.getAllAdmins();
    }

    /** Get admin by AdminId */
    @GetMapping("/adminId/{adminId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAdmin(@PathVariable String adminId) {
        log.info("AdminController::getAdmin {}", adminId);
        return adminService.getAdminById(adminId);
    }

    /** Get admin by name */
    @GetMapping("/name/{name}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAdminByName(@PathVariable String name) {
        log.info("AdminController::getAdminByName {}", name);
        return adminService.getAdminByName(name);
    }

    /** Update admin */
    @PutMapping("update/{adminId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateAdmin(
            @PathVariable String adminId,
            @RequestBody AdminRequest request) {
        log.info("AdminController::updateAdmin {}", adminId);
        return adminService.updateAdminById(adminId, request);
    }

    /** Delete admin */
    @DeleteMapping("delete/{adminId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteAdmin(@PathVariable String adminId) {
        log.info("AdminController::deleteAdmin {}", adminId);
        return adminService.deleteAdminById(adminId);
    }

    /** Reset admin password */
    @PostMapping("/reset-password/{adminId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> resetPassword(@PathVariable String adminId,AdminPasswordResetRequest request) {
        log.info("AdminController::resetPassword {}", adminId);
        return adminService.resetAdminPassword(adminId,request);
    }
}
