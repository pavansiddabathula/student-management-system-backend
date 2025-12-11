package com.techcode.studentmgmt.dto.requestdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobCreateRequest {
    private String title;
    private String companyName;
    private String jobType; // FULL_TIME, PART_TIME, INTERNSHIP
    private String location;
    private String description;
    private String salaryPackage;
    private String pdfUrl;     // Later will replace with file upload
}

