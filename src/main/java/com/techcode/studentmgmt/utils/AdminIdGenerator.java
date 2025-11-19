package com.techcode.studentmgmt.utils;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class AdminIdGenerator {

    private static final String CHAR_SET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public String generateAdminId() {
        SecureRandom random = new SecureRandom();
        StringBuilder id = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            id.append(CHAR_SET.charAt(random.nextInt(CHAR_SET.length())));
        }
        return id.toString();
    }
}
