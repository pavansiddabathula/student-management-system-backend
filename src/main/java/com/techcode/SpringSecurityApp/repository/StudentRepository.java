package com.techcode.SpringSecurityApp.repository;



import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techcode.SpringSecurityApp.entity.StudentInfo;
@Repository
public interface StudentRepository extends JpaRepository<StudentInfo, Long> {
	
    Optional<StudentInfo> findByUsername(String username);
    Optional<StudentInfo> findByRollNumber(String rollNumber);
    Optional<StudentInfo> findByEmail(String email);

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    void deleteByRollNumber(String rollNumber);
	
  
}
