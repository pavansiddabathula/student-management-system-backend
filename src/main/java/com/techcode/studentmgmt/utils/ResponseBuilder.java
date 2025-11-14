package com.techcode.studentmgmt.utils;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.techcode.studentmgmt.dto.responsedto.SuccessResponse;

public class ResponseBuilder {

    private ResponseBuilder() {}

    public static ResponseEntity<SuccessResponse> success(String message) {
        return ResponseEntity.ok(
            SuccessResponse.builder()
                .status("SUCCESS")
                .message(message)
                .timestamp(LocalDateTime.now())
                .build()
        );
    }

    public static ResponseEntity<SuccessResponse> success(String message, Object data) {
        return ResponseEntity.ok(
            SuccessResponse.builder()
                .status("SUCCESS")
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build()
        );
    }

    public static ResponseEntity<SuccessResponse> success(String message, Object data, HttpStatus status) {
        return ResponseEntity.status(status)
            .body(
                SuccessResponse.builder()
                    .status("SUCCESS")
                    .message(message)
                    .data(data)
                    .timestamp(LocalDateTime.now())
                    .build()
            );
    }
}
