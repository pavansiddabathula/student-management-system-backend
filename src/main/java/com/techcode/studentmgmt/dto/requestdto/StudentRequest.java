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
    private String firstName;
    
    @NotBlank(message = "Last name must not be empty")
    private String lastName;
    
   
    @NotBlank(message = "Email must not be empty")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone number must not be empty")
    @Pattern(regexp = "\\d{10}", message = "Phone number must be exactly 10 digits")
    private String phoneNumber;

    @NotBlank(message = "Branch must not be empty")
    private String branch;
}
