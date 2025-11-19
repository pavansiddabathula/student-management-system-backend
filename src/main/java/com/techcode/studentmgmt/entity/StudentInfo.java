package com.techcode.studentmgmt.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "student_info")
public class StudentInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10, unique = true, nullable = false)
    private String rollNumber;

    @Column(length = 100, nullable = false)
    private String fullName;

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
