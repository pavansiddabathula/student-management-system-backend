package com.techcode.studentmgmt.dto.requestdto;

import lombok.Data;

@Data
public class AdminPasswordResetRequest {
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}
