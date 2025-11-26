package com.techcode.studentmgmt.dto.requestdto;

import lombok.Data;

@Data
public class StudentLoginRequest {
    private String rollNumber;
    private String password;
}
