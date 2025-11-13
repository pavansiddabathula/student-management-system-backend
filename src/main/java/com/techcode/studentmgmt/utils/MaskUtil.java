package com.techcode.studentmgmt.utils;

public final class MaskUtil {

    private MaskUtil() {
        // private constructor to prevent instantiation
    }
    public static String maskPhone(String phone) {
        if (phone == null || phone.length() < 4) {
            return "****";
        }
        // Mask all but last 4 digits
        return "****" + phone.substring(phone.length() - 4);
    }
}