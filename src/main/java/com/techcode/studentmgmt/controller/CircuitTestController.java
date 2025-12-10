package com.techcode.studentmgmt.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
@Slf4j
public class CircuitTestController {

    private final RestTemplate restTemplate;

    @GetMapping("/success")
    @CircuitBreaker(name = "studmgmt", fallbackMethod = "fallbackResponse")
    public ResponseEntity<?> failCall() {
        log.info("Inside failCall - Attempting external service call...");
        
        String response = restTemplate.getForObject(
                "http://localhost:8081/v1/paypal/sub",
                String.class
        );

        return ResponseEntity.ok(
            Map.of(
                "status", "SUCCESS",
                "message", "External service responded successfully",
                "data", response
            )
        );
    }

    /**
     * Fallback method executed when circuit breaker opens or external call fails
     */
    public ResponseEntity<?> fallbackResponse(Throwable t) {
        log.warn("⚠ CircuitBreaker Active | Fallback executed | Reason: {}", t.toString());

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
            .body(
                Map.of(
                    "status", "FAILED",
                    "message", "Payment Service temporarily unavailable",
                    "fallback", true,
                    "error", t.getMessage()
                )
            );
    }
}
