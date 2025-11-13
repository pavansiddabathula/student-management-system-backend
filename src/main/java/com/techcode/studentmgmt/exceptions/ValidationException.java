package com.techcode.studentmgmt.exceptions;

import java.util.Map;
import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
    private final Map<String, String> errors;

    public ValidationException(Map<String, String> errors) {
        super("Validation failed");
        this.errors = errors;
    }
}
