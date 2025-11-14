package com.techcode.studentmgmt.dto.responsedto;

import java.time.LocalDateTime;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {

    private String status; // e.g. "FAILURE" or "ERROR"
    private String errorCode;
    private String errorMessage;
    private LocalDateTime timestamp;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, String> fieldErrors;
}
