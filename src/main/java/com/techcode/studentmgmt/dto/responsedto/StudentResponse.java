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
    private String fullName;
    private String email;
    private String branch;
    private String phoneNumber;
}
