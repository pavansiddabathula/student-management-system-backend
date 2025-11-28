package com.techcode.studentmgmt.dto.requestdto;

import lombok.Data;

@Data
public class ForgotPasswordRequest {
    // Roll or Admin ID
	private String identifier;
    private String email;
    
}