package com.techcode.studentmgmt.utils;

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

	
	public void sendPasswordMail(String to, String name, String userId, String password, String role) {

		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

			helper.setTo(to);
			helper.setSubject("Your Login Credentials – Student Portal Access");

			String htmlContent = """
					<html>
					<body style="font-family: Arial, sans-serif; background-color: #f5f6fa; padding: 20px;">

					<div style="max-width: 600px; margin: auto; background: #ffffff; border-radius: 10px;
					            padding: 30px; box-shadow: 0 4px 10px rgba(0,0,0,0.1);">

					    <h2 style="color: #2d3436; text-align: center; margin-bottom: 10px;">
					        Student Portal – Account Created
					    </h2>

					    <p style="font-size: 15px; color: #2d3436;">
					        Hello <strong>%s</strong>,<br><br>
					        Your <strong>%s</strong> account has been successfully created.<br><br>
					        Below are your login credentials:
					    </p>

					    <table style="width: 100%%; border-collapse: collapse; margin-top: 20px;">
					        <tr>
					            <td style="padding: 10px; background: #dfe6e9; font-weight: bold;">User ID</td>
					            <td style="padding: 10px; background: #f1f2f6;">%s</td>
					        </tr>
					        <tr>
					            <td style="padding: 10px; background: #dfe6e9; font-weight: bold;">Password</td>
					            <td style="padding: 10px; background: #f1f2f6;">%s</td>
					        </tr>
					    </table>

					    <div style="margin-top: 25px; padding: 15px; background: #fff9db; border-left: 4px solid #fdcb6e;">
					        <p style="margin: 0; font-size: 14px; color: #2d3436;">
					            <strong>Important:</strong><br><br>
					            • This is a system-generated password.<br>
					            • You will be prompted to change your password after logging in.<br>
					            • For your safety, do not share these details with anyone.
					        </p>
					    </div>

					    <p style="font-size: 15px; color: #2d3436; margin-top: 25px;">
					        If you face any login issues, feel free to reach out to our support team.
					    </p>

					    <hr style="margin: 25px 0; border: none; border-top: 1px solid #b2bec3;">

					    <p style="font-size: 14px; color: #636e72; text-align: center;">
					        Regards,<br>
					        <strong>TechCode Solutions – Student Portal Team</strong><br>
					        Powered by Spring Boot | Secure | Modern
					    </p>

					</div>

					</body>
					</html>
					""".formatted(name, role, userId, password);


			helper.setText(htmlContent, true); // true = HTML enabled
			mailSender.send(message);

			log.info("📧 HTML email sent successfully to {}", to);

		} catch (Exception e) {
			log.error("❌ Failed to send HTML email to {}: {}", to, e.getMessage());
		}
	}

	public void sendOtpMail(String toEmail, String fullName, String otp) {
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

			helper.setTo(toEmail);
			helper.setSubject("Password Reset OTP - Student Portal");

			String body = "<html>" + "<body>" + "<h3>Hello " + fullName + ",</h3>"
					+ "<p>Your OTP to reset your password is:</p>" + "<h2 style='color:#007bff; font-size: 28px;'>"
					+ otp + "</h2>" + "<p>This OTP is valid for <b>5 minutes</b>. Do not share it with anyone.</p>"
					+ "<br/>" + "<p>Regards,</p>" + "<p><b>Student Portal Team</b></p>" + "</body>" + "</html>";

			helper.setText(body, true);

			mailSender.send(mimeMessage);
			log.info("OTP mail sent successfully to {}", toEmail);
		} catch (MessagingException e) {
			log.error("Failed to send OTP Email to {}", toEmail, e);
			throw new RuntimeException("Failed to send OTP email. Please try again.");
		}
	}
	
	
	@Async
	public void sendPasswordChangeAlert(String to, String name, String userId, String role) {

	    try {
	        MimeMessage message = mailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

	        helper.setTo(to);
	        helper.setSubject("Password Updated Successfully – Student Portal Security Alert");

	        String htmlContent = """
	                <html>
	                <body style="font-family: Arial, sans-serif; background-color: #f5f6fa; padding: 20px;">

	                <div style="max-width: 600px; margin: auto; background: #ffffff; border-radius: 10px;
	                            padding: 30px; box-shadow: 0 4px 10px rgba(0,0,0,0.1);">

	                    <h2 style="color: #2d3436; text-align: center; margin-bottom: 10px;">
	                        Password Changed Successfully
	                    </h2>

	                    <p style="font-size: 15px; color: #2d3436;">
	                        Hello <strong>%s</strong>,<br><br>
	                        Your password for your <strong>%s</strong> account has been updated successfully.<br><br>
	                        Account ID:<strong> %s</strong>
	                    </p>

	                    <div style="margin-top: 25px; padding: 15px; background: #ffeaa7; border-left: 4px solid #fab1a0;">
	                        <p style="margin: 0; font-size: 14px; color: #2d3436;">
	                            <strong>Security Notice:</strong><br><br>
	                            • If you made this change, no further action is required.<br>
	                            • If you did <strong>NOT</strong> make this change, please report it immediately.<br>
	                            • For your safety, avoid sharing your password with anyone.
	                        </p>
	                    </div>

	                    <p style="font-size: 15px; color: #2d3436; margin-top: 25px;">
	                        If you face any issues, feel free to reach out to our support team.
	                    </p>

	                    <hr style="margin: 25px 0; border: none; border-top: 1px solid #b2bec3;">

	                    <p style="font-size: 14px; color: #636e72; text-align: center;">
	                        Regards,<br>
	                        <strong>TechCode Solutions – Student Portal Team</strong><br>
	                        Secure • Reliable • Modern
	                    </p>

	                </div>

	                </body>
	                </html>
	                """.formatted(name, role, userId);

	        helper.setText(htmlContent, true);
	        mailSender.send(message);

	        log.info("📧 Password change alert sent successfully to {}", to);

	    } catch (Exception e) {
	        log.error("❌ Failed to send password change alert to {}: {}", to, e.getMessage());
	    }
	}

}
