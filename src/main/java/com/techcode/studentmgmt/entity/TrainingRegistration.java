package com.techcode.studentmgmt.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.techcode.studentmgmt.enums.RegistrationStatus;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "training_registration",
    uniqueConstraints = @UniqueConstraint(
        columnNames = {"trainingCode", "rollNumber"}
    ),
    indexes = {
        @Index(columnList = "trainingCode"),
        @Index(columnList = "rollNumber")
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // -------- Value-based references --------

    @Column(nullable = false, length = 30)
    private String trainingCode;   // from Training table

    @Column(nullable = false, length = 10)
    private String rollNumber;     // from StudentInfo table

    // -------- Snapshot fields (optional but useful) --------

    @Column(nullable = false, length = 150)
    private String trainingName;

    @Column(nullable = false, length = 100)
    private String studentName;

    // -------- Registration info --------

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RegistrationStatus registrationStatus;

    @Column(nullable = false)
    private Boolean paid;

    @Column(nullable = false)
    private BigDecimal amountPaid;
    
    @Column(name = "training_price", nullable = false)
    private BigDecimal trainingPrice;


    @Column(nullable = false)
    private LocalDateTime registeredAt;
}
