package com.techcode.SpringSecurityApp.controller;



import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/add")
@Slf4j
public class AdditionController {

	
    

    @PostMapping("/two-numbers/{a}/{b}")
    public ResponseEntity<Integer> addTwoNumbers(@PathVariable int a, @PathVariable int b) {
        int sum = a + b;
        log.info("Addition of {} and {} is: {}", a, b, sum);
        return ResponseEntity.ok(sum);
    }
}
