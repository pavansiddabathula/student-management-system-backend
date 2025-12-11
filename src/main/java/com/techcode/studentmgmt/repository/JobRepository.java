package com.techcode.studentmgmt.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.techcode.studentmgmt.entity.JobInfo;

@Repository
public interface JobRepository extends JpaRepository<JobInfo, Long> {

    Optional<JobInfo> findByJobId(String jobId);

    @Query("SELECT j FROM JobInfo j WHERE j.status = 'OPEN'")
    List<JobInfo> findOpenJobs();
    
    @Query("SELECT j.jobId FROM JobInfo j WHERE j.jobId LIKE CONCAT('JOB-', :year, '-%') ORDER BY j.id DESC LIMIT 1")
    String findLastJobIdForYear(@Param("year") String year);
    
    Optional<JobInfo> findByTitleAndCompanyNameAndJobTypeAndLocation(
            String title, String companyName, String jobType, String location);

	boolean existsByJobId(String jobId);

}
