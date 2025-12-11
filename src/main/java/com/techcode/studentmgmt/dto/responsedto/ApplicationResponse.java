package com.techcode.studentmgmt.dto.responsedto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationResponse {

	
	private Long id;
    private Long applicationId;

    private String jobId;
    private String rollNumber;

    private String studentName;   // fetched separately
    private String resumeUrl;

    private String status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime appliedAt;
}

