package com.techcode.studentmgmt.dto.responsedto;

import com.techcode.studentmgmt.entity.AdminInfo;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminResponse {

    private String status;       // SUCCESS
    private Long id;             // Primary Key
    private String adminId;      // Unique 6-char ID
    private String name;
    private String email;
    private String phoneNumber;
    private String role;         // Always ADMIN
}