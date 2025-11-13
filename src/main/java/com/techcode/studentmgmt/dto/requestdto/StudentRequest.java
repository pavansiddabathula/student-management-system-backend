package com.techcode.studentmgmt.dto.requestdto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentRequest {


    @NotBlank(message = "Roll number must not be empty")
    @Size(min = 10, max = 10, message = "Roll number must be exactly 10 characters")
    private String rollNumber;

    @NotBlank(message = "First name must not be empty")
    @Size(max = 50, message = "First name cannot exceed 50 characters")
    private String firstName;

    @NotBlank(message = "Last name must not be empty")
    @Size(max = 50, message = "Last name cannot exceed 50 characters")
    private String lastName;

    @NotBlank(message = "Email must not be empty")
    @Email(message = "Please enter a valid email address")
    private String email;

    @NotBlank(message = "Username must not be empty")
    @Size(max = 30, message = "Username cannot exceed 30 characters")
    private String username;

    @Pattern(regexp = "\\d{10}", message = "Phone number must be 10 digits")
    private String phoneNumber;

    @NotBlank(message = "Branch must not be empty")
    private String branch;

    @NotBlank(message = "Password must not be empty")
    private String password;

    @NotBlank(message = "Confirm Password must not be empty")
    private String confirmPassword;
}
