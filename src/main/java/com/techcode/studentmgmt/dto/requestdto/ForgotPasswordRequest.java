package com.techcode.studentmgmt.dto.requestdto;

import lombok.Data;

@Data
public class ForgotPasswordRequest {
    private String identifier; // Roll or Admin ID
    private String email;
    
}