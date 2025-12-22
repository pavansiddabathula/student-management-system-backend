package com.techcode.studentmgmt.dto.responsedto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActionLinks {

    private String payNowUrl;
    private String skipPaymentUrl;
}
