package com.techcode.studentmgmt.dto.responsedto;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SuccessResponse {
    private String status;          // SUCCESS / FAILURE
    private String message;         // e.g., "Student registered successfully"
    private LocalDateTime timestamp;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object data;            // Actual payload (StudentResponse / list)
}
