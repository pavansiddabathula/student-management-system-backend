package com.techcode.studentmgmt.exceptions;
import com.techcode.studentmgmt.constants.ErrorCodeEnums;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final ErrorCodeEnums errorCode;
    private final String formattedMessage;

    public BusinessException(ErrorCodeEnums errorCode, String value) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.formattedMessage = String.format(errorCode.getMessage(), value);
    }
}