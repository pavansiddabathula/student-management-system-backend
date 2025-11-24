package com.techcode.studentmgmt.service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class OtpService {

    private final Map<String, String> otpStorage = new ConcurrentHashMap<>();
    private final Map<String, LocalDateTime> expiryStorage = new ConcurrentHashMap<>();
    private final Map<String, Boolean> otpVerifiedStorage = new ConcurrentHashMap<>();

    private static final SecureRandom secureRandom = new SecureRandom();

    /* Generate OTP */
    public String generateOtp(String identifier) {
        int otpValue = secureRandom.nextInt(900000) + 100000; // 6-digit OTP
        String otp = String.valueOf(otpValue);

        otpStorage.put(identifier, otp);
        expiryStorage.put(identifier, LocalDateTime.now().plusMinutes(5));
        otpVerifiedStorage.put(identifier, false); // reset verification status

        return otp;
    }

    /* Validate OTP correctness & expiry */
    public boolean validateOtp(String identifier, String otp) {

        if (!otpStorage.containsKey(identifier)) {
            return false;
        }

        if (LocalDateTime.now().isAfter(expiryStorage.get(identifier))) {
        	clearVerification(identifier);
            return false;
        }

        boolean isValid = otpStorage.get(identifier).equals(otp);

        if (isValid) {
            otpVerifiedStorage.put(identifier, true);  // mark verified
            otpStorage.remove(identifier);             // remove OTP
        }

        return isValid;
    }

    /* Check if OTP already verified */
    public boolean isOtpVerified(String identifier) {
        return otpVerifiedStorage.getOrDefault(identifier, false);
    }

    /* Clear verification & expiry after password update */
    public void clearVerification(String identifier) {
        otpStorage.remove(identifier);
        expiryStorage.remove(identifier);
        otpVerifiedStorage.remove(identifier);
    }
}
