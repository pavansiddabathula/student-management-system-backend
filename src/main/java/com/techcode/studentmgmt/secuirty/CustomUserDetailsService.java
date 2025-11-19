package com.techcode.studentmgmt.secuirty;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.techcode.studentmgmt.entity.StudentInfo;
import com.techcode.studentmgmt.repository.StudentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final StudentRepository studentRepository;

    @Override
    public UserDetails loadUserByUsername(String rollNumber) {
        StudentInfo student = studentRepository.findByRollNumber(rollNumber)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new CustomUserDetails(student);
    }
}
