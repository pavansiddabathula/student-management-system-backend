package com.techcode.studentmgmt.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.techcode.studentmgmt.constants.ApplicationStatus;
import com.techcode.studentmgmt.constants.ErrorCodeEnums;
import com.techcode.studentmgmt.constants.JobStatus;
import com.techcode.studentmgmt.dto.requestdto.ApplyJobRequest;
import com.techcode.studentmgmt.dto.requestdto.BulkStatusUpdateRequest;
import com.techcode.studentmgmt.dto.requestdto.JobCreateRequest;
import com.techcode.studentmgmt.dto.responsedto.ApplicationResponse;
import com.techcode.studentmgmt.dto.responsedto.JobResponse;
import com.techcode.studentmgmt.dto.responsedto.SuccessResponse;
import com.techcode.studentmgmt.entity.AdminInfo;
import com.techcode.studentmgmt.entity.JobApplication;
import com.techcode.studentmgmt.entity.JobInfo;
import com.techcode.studentmgmt.entity.StudentInfo;
import com.techcode.studentmgmt.exceptions.BusinessException;
import com.techcode.studentmgmt.exceptions.ValidationException;
import com.techcode.studentmgmt.repository.AdminRepository;
import com.techcode.studentmgmt.repository.JobApplicationRepository;
import com.techcode.studentmgmt.repository.JobRepository;
import com.techcode.studentmgmt.repository.StudentRepository;
import com.techcode.studentmgmt.utils.TimeUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final JobApplicationRepository jobApplicationRepository;
    private final AdminRepository adminRepository;
    private final StudentRepository studentRepository;
    private final TimeUtil timeUtil;
    private final AtomicInteger index;

    // ---------------------------------------------------------
    // CREATE JOB
    // ---------------------------------------------------------
    @Override
    public ResponseEntity<?> createJob(JobCreateRequest req) {

        log.info("CreateJob | Incoming Request | title={}, company={}, type={}, location={}",
                req.getTitle(), req.getCompanyName(), req.getJobType(), req.getLocation());

        validateCreateJob(req);

        jobRepository.findByTitleAndCompanyNameAndJobTypeAndLocation(
                req.getTitle(), req.getCompanyName(), req.getJobType(), req.getLocation())
                .ifPresent(e -> {
                    log.warn("CreateJob | Duplicate job detected | title={}, company={}", req.getTitle(), req.getCompanyName());
                    throw new BusinessException(ErrorCodeEnums.DUPLICATE_JOB);
                });

        AdminInfo admin = adminRepository.findByAdminId("ADM001")
                .orElseThrow(() -> {
                    log.error("CreateJob | Admin not found | adminId=ADM001");
                    return new BusinessException(ErrorCodeEnums.ACCESS_DENIED);
                });

        String jobId = generateJobId();
        log.info("CreateJob | Generated JobId={}", jobId);

        JobInfo job = JobInfo.builder()
                .jobId(jobId)
                .title(req.getTitle())
                .companyName(req.getCompanyName())
                .jobType(req.getJobType())
                .location(req.getLocation())
                .description(req.getDescription())
                .salaryPackage(req.getSalaryPackage())
                .pdfUrl(req.getPdfUrl())
                .createdBy(admin)
                .createdAt(LocalDateTime.now())
                .status(JobStatus.OPEN)
                .build();

        jobRepository.save(job);

        log.info("CreateJob | Saved Successfully | jobId={}", jobId);

        return success("Job created successfully", Map.of("jobId", jobId), HttpStatus.CREATED);
    }

    // ---------------------------------------------------------
    // FETCH ALL OPEN JOBS
    // ---------------------------------------------------------
    @Override
    public ResponseEntity<?> getJobs() {

        log.info("GetJobs | Fetching all OPEN jobs");

        List<JobResponse> jobs = jobRepository.findOpenJobs()
                .stream()
                .map(job -> JobResponse.builder()
                        .jobId(job.getJobId())
                        .title(job.getTitle())
                        .description(job.getDescription())
                        .salaryPackage(job.getSalaryPackage())
                        .pdfUrl(job.getPdfUrl())
                        .createdAt(job.getCreatedAt())
                        .postedAgo(timeUtil.getTimeAgo(job.getCreatedAt()))
                        .createdBy(job.getCreatedBy().getName())
                        .status(job.getStatus().name())
                        .build())
                .toList();

        log.info("GetJobs | TotalJobs={}", jobs.size());

        return success("Jobs fetched successfully", jobs);
    }

    @Override
    public ResponseEntity<?> viewApplications(String jobId) {

        log.info("ViewApplications | Checking job existence | jobId={}", jobId);

        // Check job existence
        boolean exists = jobRepository.existsByJobId(jobId);
        if (!exists) {
            log.warn("ViewApplications | Job Not Found | jobId={}", jobId);
            throw new BusinessException(ErrorCodeEnums.JOB_NOT_FOUND, jobId);
        }

        // Fetch all applications
        List<JobApplication> apps = jobApplicationRepository.findByJobId(jobId);
        log.info("ViewApplications | Found {} applications | jobId={}", apps.size(), jobId);

        List<ApplicationResponse> responses = apps.stream().map(app -> {

            StudentInfo student = studentRepository.findByRollNumber(app.getRollNumber()).orElse(null);
            String name = (student != null)
                    ? student.getFirstName() + " " + student.getLastName()
                    : "Unknown";

            return ApplicationResponse.builder()
                    .id((long) index.getAndIncrement())
                    .applicationId(app.getId())
                    .jobId(app.getJobId())
                    .rollNumber(app.getRollNumber())
                    .studentName(name)
                    .resumeUrl(app.getResumeUrl())
                    .status(app.getStatus().name())
                    .appliedAt(app.getAppliedAt())
                    .build();

        }).toList();

        log.info("ViewApplications | Response prepared | jobId={} | size={}", jobId, responses.size());

        return success("Applications fetched successfully", responses);
    }


    // ---------------------------------------------------------
    // APPLY FOR JOB
    // ---------------------------------------------------------
    @Override
    public ResponseEntity<?> applyForJob(ApplyJobRequest req) {

        log.info("ApplyForJob | jobId={}, rollNumber={}", req.getJobId(), req.getRollNumber());

        validateApplyRequest(req);

        jobApplicationRepository.findByJobIdAndRollNumber(req.getJobId(), req.getRollNumber())
                .ifPresent(a -> {
                    log.warn("ApplyForJob | Duplicate application | jobId={}, roll={}", req.getJobId(), req.getRollNumber());
                    throw new BusinessException(ErrorCodeEnums.DUPLICATE_APPLICATION);
                });

        JobApplication app = JobApplication.builder()
                .jobId(req.getJobId())
                .rollNumber(req.getRollNumber())
                .resumeUrl(req.getResumeUrl())
                .status(ApplicationStatus.APPLIED)
                .appliedAt(LocalDateTime.now())
                .build();

        jobApplicationRepository.save(app);

        log.info("ApplyForJob | Application Saved | applicationId={}, jobId={}, roll={}",
                app.getId(), req.getJobId(), req.getRollNumber());

        return success("Job applied successfully",
                Map.of("applicationId", app.getId(), "jobId", req.getJobId(), "rollNumber", req.getRollNumber()),
                HttpStatus.CREATED);
    }

    // ---------------------------------------------------------
    // GET APPLICATIONS FOR STUDENT
    // ---------------------------------------------------------
    @Override
    public ResponseEntity<?> getStudentApplications(String rollNumber) {

        log.info("GetStudentApplications | rollNumber={}", rollNumber);

        StudentInfo student = studentRepository.findByRollNumber(rollNumber)
                .orElseThrow(() -> {
                    log.warn("GetStudentApplications | StudentNotFound | roll={}", rollNumber);
                    return new BusinessException(ErrorCodeEnums.STUDENT_NOT_FOUND, rollNumber);
                });

        List<JobApplication> apps = jobApplicationRepository.findByRollNumber(rollNumber);

        log.info("GetStudentApplications | TotalApplications={} | roll={}", apps.size(), rollNumber);

        List<ApplicationResponse> responses = apps.stream().map(app ->
                ApplicationResponse.builder()
                        .id((long) index.getAndIncrement())
                        .applicationId(app.getId())
                        .jobId(app.getJobId())
                        .rollNumber(app.getRollNumber())
                        .studentName(student.getFirstName() + " " + student.getLastName())
                        .resumeUrl(app.getResumeUrl())
                        .status(app.getStatus().name())
                        .appliedAt(app.getAppliedAt())
                        .build()
        ).toList();

        log.info("GetStudentApplications | ResponsePrepared | roll={}, count={}", rollNumber, responses.size());

        return success("Student applications fetched successfully", responses);
    }

    // ---------------------------------------------------------
    // BULK STATUS UPDATE
    // ---------------------------------------------------------
    @Override
    public ResponseEntity<?> bulkUpdateApplicationStatus(BulkStatusUpdateRequest req) {

        log.info("BulkUpdate | jobId={}, newStatus={}, ids={}",
                req.getJobId(), req.getStatus(), req.getApplicationIds());

        ApplicationStatus newStatus = parseStatus(req.getStatus());

        jobRepository.findByJobId(req.getJobId())
                .orElseThrow(() -> {
                    log.warn("BulkUpdate | JobNotFound | jobId={}", req.getJobId());
                    return new BusinessException(ErrorCodeEnums.JOB_NOT_FOUND, req.getJobId());
                });

        List<JobApplication> apps = jobApplicationRepository.findAllById(req.getApplicationIds());

        if (apps.isEmpty()) {
            log.warn("BulkUpdate | NoApplicationsFound | ids={}", req.getApplicationIds());
            throw new BusinessException(ErrorCodeEnums.APPLICATION_NOT_FOUND, req.getApplicationIds());
        }

        List<JobApplication> validApps = apps.stream()
                .filter(a -> a.getJobId().equals(req.getJobId()))
                .toList();

        if (validApps.isEmpty()) {
            log.warn("BulkUpdate | NoApplicationsForJob | jobId={}", req.getJobId());
            throw new BusinessException(ErrorCodeEnums.APPLICATION_NOT_FOUND, req.getJobId());
        }

        validApps.forEach(a -> a.setStatus(newStatus));
        jobApplicationRepository.saveAll(validApps);

        log.info("BulkUpdate | Updated {} applications | jobId={}, status={}",
                validApps.size(), req.getJobId(), newStatus);

        return success("Application status updated",
                Map.of("updatedCount", validApps.size(), "status", newStatus.name(), "jobId", req.getJobId()));
    }

    // ---------------------------------------------------------
    // HELPERS (unchanged except logs added above)
    // ---------------------------------------------------------

    private void validateCreateJob(JobCreateRequest req) {
        if (isBlank(req.getTitle()) || isBlank(req.getCompanyName()) || isBlank(req.getJobType()))
            throw new ValidationException(Map.of("fields", "Missing required job fields"));
    }

    private void validateApplyRequest(ApplyJobRequest req) {
        if (isBlank(req.getJobId()) || isBlank(req.getRollNumber()) || isBlank(req.getResumeUrl()))
            throw new ValidationException(Map.of("fields", "Missing required fields"));
    }

    private ApplicationStatus parseStatus(String status) {
        try {
            return ApplicationStatus.valueOf(status.toUpperCase());
        } catch (Exception e) {
            log.warn("ParseStatus | Invalid status={}", status);
            throw new BusinessException(ErrorCodeEnums.VALIDATION_ERROR, "Invalid status");
        }
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private String generateJobId() {

        String year = String.valueOf(LocalDate.now().getYear()).substring(2);
        String last = jobRepository.findLastJobIdForYear(year);

        int next = (last == null) ? 1 : Integer.parseInt(last.substring(last.lastIndexOf("-") + 1)) + 1;

        return "JOB-" + year + "-" + String.format("%06d", next);
    }

    private ResponseEntity<SuccessResponse> success(String message, Object data) {
        return success(message, data, HttpStatus.OK);
    }

    private ResponseEntity<SuccessResponse> success(String message, Object data, HttpStatus status) {
        return ResponseEntity.status(status).body(
                SuccessResponse.builder()
                        .status("SUCCESS")
                        .message(message)
                        .timestamp(LocalDateTime.now())
                        .data(data)
                        .build()
        );
    }
}
