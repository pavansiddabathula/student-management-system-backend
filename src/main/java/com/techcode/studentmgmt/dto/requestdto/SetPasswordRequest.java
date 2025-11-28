package com.techcode.studentmgmt.dto.requestdto;

import lombok.Data;

@Data
public class SetPasswordRequest {
	
	private String newPassword;
	private String confirmPassword;
	private String identifier;
}
