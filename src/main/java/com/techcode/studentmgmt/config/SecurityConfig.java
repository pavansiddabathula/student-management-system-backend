package com.techcode.studentmgmt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

import com.techcode.studentmgmt.secuirty.CustomAccessDeniedHandler;
import com.techcode.studentmgmt.secuirty.CustomAuthenticationEntryPoint;
import com.techcode.studentmgmt.secuirty.JwtFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtFilter jwtFilter;
	private final CustomAccessDeniedHandler customAccessDeniedHandler;
	private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

	    http
        .cors(Customizer.withDefaults()) // Enable CORS
        .csrf().disable()
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(
                "/api/auth/**",
                "/api/students/create",
                "/api/admin/create",
                "/api/auth/forgot-password",
                "/api/auth/verify-otp",
                "/api/auth/set-password",
                "/test/**",
                "/actuator/**"
            ).permitAll()
            .requestMatchers("/api/admin/**").hasRole("ADMIN")
            .requestMatchers("/api/students/**").hasAnyRole("STUDENT", "ADMIN")
            .anyRequest().authenticated()
        )
        .exceptionHandling(ex -> ex
            .authenticationEntryPoint(customAuthenticationEntryPoint)
            .accessDeniedHandler(customAccessDeniedHandler)
        )
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
   }

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(8);
	}
	@Bean
	public RestTemplate restTemplate() {
	    return new RestTemplate();
	}

}
