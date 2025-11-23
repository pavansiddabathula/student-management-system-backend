package com.techcode.studentmgmt.utils;

import org.springframework.stereotype.Component;

@Component
public class AdminIdGenerator {

    private static final String PREFIX = "ADM";
    private static final int DIGITS = 3;

    public String generateAdminId(String lastId) {

        if (lastId == null) {
            return PREFIX + String.format("%0" + DIGITS + "d", 1);
        }

        int number = Integer.parseInt(lastId.substring(PREFIX.length()));
        number++;

        return PREFIX + String.format("%0" + DIGITS + "d", number);
    }
}
