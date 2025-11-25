package com.techcode.studentmgmt.exceptions;
import com.techcode.studentmgmt.constants.ErrorCodeEnums;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private final ErrorCodeEnums errorCode;

    public BusinessException(ErrorCodeEnums errorCode, Object... args) {
        super(String.format(errorCode.getMessage(), args));  // FORMAT THE MESSAGE HERE
        this.errorCode = errorCode;
    }
}