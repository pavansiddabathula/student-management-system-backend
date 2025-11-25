package com.techcode.studentmgmt.secuirty;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private static final long EXPIRY_DURATION = 7; // hours

    // Use 256-bit secret key
    private final Key key = Keys.hmacShaKeyFor(
            "THIS_IS_MY_SUPER_SECRET_KEY_FOR_JWT_256_BIT_SPRING_BOOT_2025_XYZ".getBytes()
    );

    public String generateToken(String subject, Map<String, Object> claims) {

        Instant now = Instant.now();
        Instant expiryTime = now.plus(EXPIRY_DURATION, ChronoUnit.HOURS);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiryTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException ex) {
            return false;
        }
    }

    public long getExpiryInSeconds() {
        return EXPIRY_DURATION * 60 * 60;
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
