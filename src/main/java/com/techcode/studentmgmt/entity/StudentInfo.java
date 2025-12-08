package com.techcode.studentmgmt.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
    name = "student_info",
    indexes = {
        @Index(name = "idx_student_rollNumber", columnList = "rollNumber"),
        @Index(name = "idx_student_email", columnList = "email"),
        @Index(name = "idx_student_lastName", columnList = "lastName")
    }
)
public class StudentInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10, unique = true, nullable = false)
    private String rollNumber;

    @Column(length = 50, nullable = false)
    private String firstName;


    @Column(length = 50, nullable = false)
    private String lastName;

    @Column(length = 100, nullable = false, unique = true)
    private String email;

    @Column(length = 10, nullable = false)
    private String phoneNumber;

    @Column(length = 10, nullable = false)
    private String branch;

    @Column(length = 120, nullable = false)
    private String password;

    @Builder.Default
    @Column(nullable = false, length = 20)
    private String role = "STUDENT";
}
