package com.techcode.studentmgmt.dto.requestdto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AdminLoginRequest {

    @NotBlank(message = "Admin ID is required")
    @Size(min = 6, max = 6, message = "Admin ID must be exactly 6 characters")
    private String adminid;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;
}

