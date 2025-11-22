package com.techcode.studentmgmt.secuirty;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.techcode.studentmgmt.entity.AdminInfo;
import com.techcode.studentmgmt.entity.StudentInfo;
import com.techcode.studentmgmt.repository.AdminRepository;
import com.techcode.studentmgmt.repository.StudentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final StudentRepository studentRepository;
    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {

        log.info("🔍 Attempting to load user by identifier: {}", identifier);

        // First check admin table
        AdminInfo admin = adminRepository.findByAdminId(identifier).orElse(null);
        if (admin != null) {
            log.info("🟢 Admin found with adminId: {}", identifier);
            return new AdminCustomUserDetails(admin);
        }

        // Then check student table
        StudentInfo student = studentRepository.findByRollNumber(identifier).orElse(null);
        if (student != null) {
            log.info("🟢 Student found with rollNumber: {}", identifier);
            return new StudentCustomUserDetails(student);
        }

        log.error("❌ User not found with identifier: {}", identifier);
        throw new UsernameNotFoundException("User not found with identifier: " + identifier);
    }
}
