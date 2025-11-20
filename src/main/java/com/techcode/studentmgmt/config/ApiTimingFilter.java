package com.techcode.studentmgmt.config;


import java.io.IOException;

import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ApiTimingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        long start = System.currentTimeMillis();

        chain.doFilter(request, response);

        long end = System.currentTimeMillis();

        HttpServletRequest req = (HttpServletRequest) request;
        log.info("API Time :: [{} {}] took {} ms",
                req.getMethod(),
                req.getRequestURI(),
                (end - start));
    }
}
