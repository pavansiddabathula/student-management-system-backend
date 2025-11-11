package com.techcode.SpringSecurityApp.dto.rsponsedto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudentResponse {

    private Long id;
    private String rollNumber;
    private String firstName;
    private String lastName;
    private String email;
    private String branch;
    private String username;
    private String phoneNumber;
}
