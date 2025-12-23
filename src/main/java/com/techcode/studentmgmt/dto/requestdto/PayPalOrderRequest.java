package com.techcode.studentmgmt.dto.requestdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
	
public class PayPalOrderRequest {

	    private String tnref;
	    private String amount;
	    private String currency;
	    private String returnUrl;
	    private String cancelUrl;
	}
