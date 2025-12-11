package com.techcode.studentmgmt.service;

import org.springframework.http.ResponseEntity;

import com.techcode.studentmgmt.dto.requestdto.ApplyJobRequest;
import com.techcode.studentmgmt.dto.requestdto.BulkStatusUpdateRequest;
import com.techcode.studentmgmt.dto.requestdto.JobCreateRequest;

public interface JobService {

    ResponseEntity<?> createJob(JobCreateRequest jobCreateRequest);

	ResponseEntity<?> getJobs();

	ResponseEntity<?> viewApplications(String jobId);
	
	ResponseEntity<?> applyForJob(ApplyJobRequest request);

	ResponseEntity<?> bulkUpdateApplicationStatus(BulkStatusUpdateRequest request);
    
	ResponseEntity<?> getStudentApplications(String rollNumber);


    
}

