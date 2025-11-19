package com.techcode.studentmgmt.repository;

import com.techcode.studentmgmt.entity.AdminInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AdminRepository extends JpaRepository<AdminInfo, Long> {

    // Find admin using system-generated admin ID
    Optional<AdminInfo> findByAdminId(String adminId);

    // Find admin using email (used for login, validation)
    Optional<AdminInfo> findByEmail(String email);

    // Find admins by name (useful if multiple admins have same name)
    List<AdminInfo> findByNameIgnoreCase(String name);

    // Check if phone number already exists
    Optional<AdminInfo> findByPhoneNumber(String phoneNumber);
}
