package com.techcode.studentmgmt.secuirty;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    private final Key key = Keys.hmacShaKeyFor("REPLACE_THIS_WITH_YOUR_256_BIT_SECRET_KEY_XYZ1234567".getBytes());
    private final long expiry = 1000 * 60 * 30; // 30 minutes
    
    private final long jwtExpirationMs = 1000 * 60 * 15; // 15 minutes


    public String generateToken(String rollNumber, Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(rollNumber)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiry))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException ex) {
            return false;
        }
    }
    
    public long getExpiryInSeconds() {
        return jwtExpirationMs / 1000;
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
