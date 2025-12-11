package com.techcode.studentmgmt.entity;

import java.time.LocalDateTime;

import com.techcode.studentmgmt.constants.JobStatus;

import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(
    name = "job_info",
    indexes = {
        @Index(columnList = "jobId", name = "idx_job_jobId"),
        @Index(columnList = "companyName", name = "idx_company"),
        @Index(columnList = "jobType", name = "idx_jobType")
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 40)
    private String jobId;      // JOB-00001

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, length = 100)
    private String companyName;

    @Column(nullable = false, length = 50)
    private String jobType; // FULL_TIME / PART_TIME / INTERNSHIP

    @Column(length = 100)
    private String location;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 50)
    private String salaryPackage;

    @Column(length = 512)
    private String pdfUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_admin_id", nullable = false)
    private AdminInfo createdBy;

    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;

    @Enumerated(EnumType.STRING)
    private JobStatus status;
}
