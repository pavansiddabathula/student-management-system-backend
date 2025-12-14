package com.techcode.studentmgmt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

	 @Bean
	    public WebMvcConfigurer corsConfigurer() {
	        return new WebMvcConfigurer() {
	            @Override
	            public void addCorsMappings(CorsRegistry registry) {
	                registry.addMapping("/**")
	                        .allowedOrigins(
	                            "http://college-portal-frontend.s3-website.ap-south-1.amazonaws.com",
	                            "http://localhost:3000",
	                            "https://d340lm6vz9i7bs.cloudfront.net",
	                            "http://13.204.231.22",
	                            "https://college-placement-portal-lovat.vercel.app"
	                        )
	                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
	                        .allowedHeaders("*")
	                        .allowCredentials(true);
	            }
	        };
	    }
	 
	 
}
