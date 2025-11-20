package com.techcode.studentmgmt.dto.responsedto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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