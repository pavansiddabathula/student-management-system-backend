package com.techcode.studentmgmt.secuirty;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techcode.studentmgmt.constants.ErrorCodeEnums;
import com.techcode.studentmgmt.dto.responsedto.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper; // Injected from Spring

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        log.error("❌ Authentication failed: {}", authException.getMessage());

        String message = "Token is missing or invalid. Please login again.";

        if ("TOKEN_EXPIRED".equals(authException.getMessage())) {
            message = "Token expired. Please login again to continue.";
        }

        ErrorResponse json = ErrorResponse.builder()
                .status("FAILURE")
                .errorCode(ErrorCodeEnums.UNAUTHORIZED.getCode())
                .errorMessage(message)
                .timestamp(LocalDateTime.now())
                .build();

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(json));
        response.getWriter().flush();
    }
}
