package com.techcode.studentmgmt.dto.responsedto;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentResponse {
	private String status;
    private Long id;            
    private String rollNumber;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String branch;
    private String phoneNumber;
}
