package com.techcode.studentmgmt.dto.requestdto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class StudentPasswordResetRequest {

    @NotBlank(message = "Old password must not be empty")
    private String oldPassword;

    @NotBlank(message = "New password must not be empty")
    private String newPassword;

    @NotBlank(message = "Confirm password must not be empty")
    private String confirmPassword;
}
