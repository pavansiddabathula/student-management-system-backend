package com.techcode.studentmgmt.dto.responsedto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobResponse {
    private String jobId;
    private String title;
    private String description;
    private String salaryPackage;
    private String pdfUrl;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    private String postedAgo;
    private String createdBy;
    private String status;
}
