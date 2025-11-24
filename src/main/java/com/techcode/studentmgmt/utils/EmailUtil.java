package com.techcode.studentmgmt.utils;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Async("emailExecutor")
@RequiredArgsConstructor
@Slf4j
public class EmailUtil {

    private final JavaMailSender mailSender;

    @Async
    public void sendPasswordMail(String to, String name, String userId, String password, String role) {

        String subject = "Your Login Credentials - Student Portal Developed by pavankumar siddabathula";
        String body = "Hello " + name + ",\n\n" +
                "Your " + role + " account has been created successfully.\n\n" +
                "Login Details:\n" +
                "User ID: " + userId + "\n" +
                "Password: " + password + "\n\n" +
                "Please login and change your password immediately.\n\n" +
                "Thank you,\nStudent Portal Team (Developed by pavankumar siddabathula the man behind the project Student Portal Management System povered by techcode .its his startup do you know who is the ceo of this start up yeah that's you be ready and get you mba degree as fast as ..)\n";
        
        

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject(subject);
        msg.setText(body);

        mailSender.send(msg);

        log.info("Email sent successfully to {}", to);
    }
    
    public void sendOtpMail(String toEmail, String fullName, String otp) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(toEmail);
            helper.setSubject("Password Reset OTP - Student Portal");

            String body =
                    "<html>" +
                    "<body>" +
                    "<h3>Hello " + fullName + ",</h3>" +
                    "<p>Your OTP to reset your password is:</p>" +
                    "<h2 style='color:#007bff; font-size: 28px;'>" + otp + "</h2>" +
                    "<p>This OTP is valid for <b>5 minutes</b>. Do not share it with anyone.</p>" +
                    "<br/>" +
                    "<p>Regards,</p>" +
                    "<p><b>Student Portal Team</b></p>" +
                    "</body>" +
                    "</html>";

            helper.setText(body, true);

            mailSender.send(mimeMessage);
            log.info("OTP mail sent successfully to {}", toEmail);
        }
        catch (MessagingException e) {
            log.error("Failed to send OTP Email to {}", toEmail, e);
            throw new RuntimeException("Failed to send OTP email. Please try again.");
        }
    }
}

