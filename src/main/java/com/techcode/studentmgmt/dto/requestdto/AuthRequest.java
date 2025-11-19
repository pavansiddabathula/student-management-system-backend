package com.techcode.studentmgmt.dto.requestdto;

import lombok.Data;

@Data
public class AuthRequest {
    private String rollNumber;
    private String password;
}
