package com.techcode.studentmgmt.auth;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AuthResponse {
    private String access_token;
    private String token_type;
    private long expires_in;
    private Instant issued_at;
    private List<String> roles;
    private Map<String, Object> user;
}
