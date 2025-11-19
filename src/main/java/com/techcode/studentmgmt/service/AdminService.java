package com.techcode.studentmgmt.service;

import com.techcode.studentmgmt.dto.requestdto.AdminPasswordResetRequest;
import com.techcode.studentmgmt.dto.requestdto.AdminRequest;
import org.springframework.http.ResponseEntity;

public interface AdminService {


        ResponseEntity<?> createAdmin(AdminRequest request);

        ResponseEntity<?> getAllAdmins();

        ResponseEntity<?> getAdminById(String adminId);

        ResponseEntity<?> getAdminByName(String name);

        ResponseEntity<?> deleteAdminById(String adminId);

        ResponseEntity<?> updateAdminById(String adminId, AdminRequest request);

        ResponseEntity<?> resetAdminPassword(String adminId,AdminPasswordResetRequest request);
    
}
