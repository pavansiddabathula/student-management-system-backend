package com.techcode.studentmgmt.utils;


import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class PasswordGeneratorUtil {

    private static final String CHAR_SET =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";

    public String generatePassword(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder pwd = new StringBuilder();

        for (int i = 0; i < length; i++) {
            pwd.append(CHAR_SET.charAt(random.nextInt(CHAR_SET.length())));
        }
        return pwd.toString();
    }
}
