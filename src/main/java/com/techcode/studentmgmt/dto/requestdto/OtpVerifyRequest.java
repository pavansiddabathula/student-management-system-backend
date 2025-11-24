package com.techcode.studentmgmt.dto.requestdto;

import lombok.Data;

@Data
public class OtpVerifyRequest {
    private String identifier;
    private String otp;
}