package com.techcode.studentmgmt.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.techcode.studentmgmt.enums.TrainingLevel;
import com.techcode.studentmgmt.enums.TrainingMode;
import com.techcode.studentmgmt.enums.TrainingStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "trainings")
public class Training {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String trainingCode;

    @Column(nullable = false, length = 150)
    private String trainingName;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false)
    private Integer durationInWeeks;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TrainingMode mode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TrainingLevel level;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    // Example: "9:00 AM - 4:00 PM"
    @Column(nullable = false, length = 50)
    private String sessionTiming;

    @Column(nullable = false)
    private Integer totalSeats;

    // 0 means free training
    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false, length = 100)
    private String trainerName;

    @Column(nullable = false, length = 500)
    private String syllabusPdfUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TrainingStatus status;

    @Column(nullable = false, length = 100)
    private String createdBy;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
