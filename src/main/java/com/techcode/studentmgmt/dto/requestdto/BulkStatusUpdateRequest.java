package com.techcode.studentmgmt.dto.requestdto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BulkStatusUpdateRequest {
	private String jobId;   
    private List<Long> applicationIds;   // multiple IDs
    private String status;               // NEW STATUS
}
