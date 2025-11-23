package com.techcode.studentmgmt.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.techcode.studentmgmt.entity.AdminInfo;

public interface AdminRepository extends JpaRepository<AdminInfo, Long> {

    // Find admin using system-generated admin ID
    Optional<AdminInfo> findByAdminId(String adminId);

    // Find admin using email (used for login, validation)
    Optional<AdminInfo> findByEmail(String email);

    // Find admins by name (useful if multiple admins have same name)
    List<AdminInfo> findByNameIgnoreCase(String name);

    // Check if phone number already exists
    Optional<AdminInfo> findByPhoneNumber(String phoneNumber);
    
    @Query(value = "SELECT admin_id FROM admin_info ORDER BY id DESC LIMIT 1", nativeQuery = true)
    String findLatestAdminId();

}
