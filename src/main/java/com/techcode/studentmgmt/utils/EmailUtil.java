package com.techcode.studentmgmt.utils;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailUtil {

    private final JavaMailSender mailSender;

    @Async
    public void sendPasswordMail(String to, String name, String userId, String password, String role) {

        String subject = "Your Login Credentials - Student Portal";
        String body = "Hello " + name + ",\n\n" +
                "Your " + role + " account has been created successfully.\n\n" +
                "Login Details:\n" +
                "User ID: " + userId + "\n" +
                "Password: " + password + "\n\n" +
                "Please login and change your password immediately.\n\n" +
                "Thank you,\nStudent Portal Team";

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject(subject);
        msg.setText(body);

        mailSender.send(msg);

        log.info("Email sent successfully to {}", to);
    }
}

