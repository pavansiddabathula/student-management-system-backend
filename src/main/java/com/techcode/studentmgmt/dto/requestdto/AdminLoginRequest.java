package com.techcode.studentmgmt.dto.requestdto;

import lombok.Data;

@Data
public class AdminLoginRequest {
	
	private String adminid;
	private String password;

}
