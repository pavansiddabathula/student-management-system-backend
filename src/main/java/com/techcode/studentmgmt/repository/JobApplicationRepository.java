package com.techcode.studentmgmt.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techcode.studentmgmt.entity.JobApplication;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    List<JobApplication> findByJobId(String jobId);

    List<JobApplication> findByRollNumber(String rollNumber);


    Optional<JobApplication> findByJobIdAndRollNumber(String jobId, String rollNumber);
}

