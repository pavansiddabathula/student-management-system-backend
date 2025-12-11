package com.techcode.studentmgmt.entity;

import java.time.LocalDateTime;

import com.techcode.studentmgmt.constants.ApplicationStatus;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "job_application",
    uniqueConstraints = @UniqueConstraint(columnNames = {"jobId", "rollNumber"}),
    indexes = {
        @Index(columnList = "jobId"),
        @Index(columnList = "rollNumber")
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 40)
    private String jobId;

    @Column(nullable = false, length = 10)
    private String rollNumber;

    @Column(length = 512)
    private String resumeUrl;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    
    private LocalDateTime appliedAt;
}
