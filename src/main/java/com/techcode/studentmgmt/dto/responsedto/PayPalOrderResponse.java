package com.techcode.studentmgmt.dto.responsedto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayPalOrderResponse {

    private String orderId;
    private String paypalStatus;
    private String redirectUrl;
}
