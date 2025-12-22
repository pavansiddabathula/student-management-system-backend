package com.techcode.studentmgmt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techcode.studentmgmt.entity.StudentInfo;
@Repository
public interface StudentRepository extends JpaRepository<StudentInfo, Long> {
	
    Optional<StudentInfo> findByRollNumber(String rollNumber);
    Optional<StudentInfo> findByEmail(String email);
    
   Optional<StudentInfo> findByfirstName(String firstName);
    

    boolean existsByEmail(String email);
    boolean existsByRollNumber(String rollNumber);

    void deleteByRollNumber(String rollNumber);
	
  
}
