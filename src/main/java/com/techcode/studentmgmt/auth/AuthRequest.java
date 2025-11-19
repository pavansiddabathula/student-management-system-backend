package com.techcode.studentmgmt.auth;

import lombok.Data;

@Data
public class AuthRequest {
    private String rollNumber;
    private String password;
}
