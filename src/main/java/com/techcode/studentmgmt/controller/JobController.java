package com.techcode.studentmgmt.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techcode.studentmgmt.dto.requestdto.ApplyJobRequest;
import com.techcode.studentmgmt.dto.requestdto.BulkStatusUpdateRequest;
import com.techcode.studentmgmt.dto.requestdto.JobCreateRequest;
import com.techcode.studentmgmt.service.JobService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/jobs")
@Slf4j
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping("/createJob")
    public ResponseEntity<?> createJob(@RequestBody JobCreateRequest request) {
    	log.info("Received request to create job: {}", request.getTitle());
        return jobService.createJob(request);
    }

    @GetMapping("/getJobs")
    public ResponseEntity<?> getJobs() {
    	log.info("Received request to get all jobs");
        return jobService.getJobs();
    }

    @GetMapping("/viewApplications")
    public ResponseEntity<?> viewApplications(@RequestParam String jobId) {
    	log.info("Received request to view applications for jobId: {}", jobId);
        return jobService.viewApplications(jobId);
    }
    
    @PostMapping("/apply")
    public ResponseEntity<?> applyJob(@RequestBody ApplyJobRequest request) {
    	log.info("Received job application from student roll number: {}", request.getJobId());
        return jobService.applyForJob(request);
    }
    
    @GetMapping("/student/applications/{rollNumber}")
    public ResponseEntity<?> getStudentApplications(@PathVariable String rollNumber) {
    	log.info("Received request to get applications for student roll number: {}", rollNumber);
        return jobService.getStudentApplications(rollNumber);
    }
    
    @PatchMapping("/applications/update-status/bulk")
    public ResponseEntity<?> bulkUpdateStatus(@RequestBody BulkStatusUpdateRequest request) {
    	log.info("Received bulk status update request for {} applications", request.getJobId());
        return jobService.bulkUpdateApplicationStatus(request);
    }




}
