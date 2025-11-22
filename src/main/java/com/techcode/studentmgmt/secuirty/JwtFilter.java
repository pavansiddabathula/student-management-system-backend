package com.techcode.studentmgmt.secuirty;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

    	try {
    	    String header = request.getHeader("Authorization");
    	    String token = null;
    	    String identifier = null;

    	    if (header != null && header.startsWith("Bearer ")) {
    	        token = header.substring(7);
    	        identifier = jwtUtil.extractUsername(token);
    	    }

    	    if (identifier != null && SecurityContextHolder.getContext().getAuthentication() == null) {
    	        UserDetails userDetails = userDetailsService.loadUserByUsername(identifier);

    	        if (jwtUtil.validateToken(token)) {
    	            UsernamePasswordAuthenticationToken authToken =
    	                    new UsernamePasswordAuthenticationToken(
    	                            userDetails, null, userDetails.getAuthorities());

    	            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    	            SecurityContextHolder.getContext().setAuthentication(authToken);
    	        }
    	    }

    	    chain.doFilter(request, response);

    	} catch (ExpiredJwtException ex) {
    	    log.error("⏳ JWT Expired: {}", ex.getMessage());
    	    customAuthenticationEntryPoint.commence(
    	            request, response, new BadCredentialsException("TOKEN_EXPIRED")
    	    );
    	    return;

    	} catch (AccessDeniedException ex) {
    	    log.error("⛔ Access Denied inside JWT filter: {}", ex.getMessage());
    	    customAccessDeniedHandler.handle(request, response, ex);
    	    

    	} catch (JwtException ex) {
    	    log.error("❌ Invalid JWT: {}", ex.getMessage());
    	    customAuthenticationEntryPoint.commence(
    	            request, response, new BadCredentialsException("INVALID_TOKEN")
    	    );
    	    return;
    	}
    }
}
