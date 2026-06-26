package com.example.TravelApp.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    // Spring Boot මඟින් application.properties වල සෙටින්ග්ස් මෙතනට auto සම්බන්ධ කරනවා
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendBookingSuccessEmail(String toEmail, String userName, String packageName, double price) {
        try {
            // 🎯 HTML Email එකක් යැවීම සඳහා MimeMessage එකක් සාදා ගැනීම
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            // 💡 ඔයා කලින් properties ෆයිල් එකට දාපු ඔයාගේ Gmail එකම මෙතනට ලියන්න
            helper.setFrom("your-email@gmail.com");
            helper.setTo(toEmail);
            helper.setSubject("Booking Confirmed! 🎉 - TravelApp");

            // 🎨 ඔයා එවපු ඉමේජ් එකේ විදිහටම හදපු ලස්සන Dark Mode HTML/CSS Content එක
            String htmlContent = "<div style=\"background-color: #1a1e21; padding: 30px; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; color: #dee2e6; max-width: 600px; margin: 0 auto; border-radius: 8px;\">" +

                    // Header (Green Bar)
                    "  <div style=\"background-color: #0f766e; color: #ffffff; padding: 20px; border-radius: 6px 6px 0 0; text-align: center; font-size: 22px; font-weight: bold;\">" +
                    "    ✓ Booking Successful!" +
                    "  </div>" +

                    // Body Content
                    "  <div style=\"padding: 20px 10px;\">" +
                    "    <p style=\"font-size: 16px; color: #ffffff;\">Hello " + userName + ",</p>" +
                    "    <p style=\"font-size: 14px; line-height: 1.6; color: #adb5bd;\">Thank you for booking with TravelApp! Your adventure has been successfully reserved and your details have been confirmed by our team.</p>" +
                    "    <p style=\"font-size: 14px; line-height: 1.6; color: #adb5bd;\">Below is a summary of your booked travel package. If you have any questions or need immediate assistance, feel free to contact us directly.</p>" +
                    "  </div>" +

                    // 🎯 Booking Summary Box (Dark Box with Green Left Border)
                    "  <div style=\"background-color: #212529; border: 1px solid #2c3034; border-left: 4px solid #0f766e; padding: 20px; border-radius: 4px; margin-bottom: 25px;\">" +
                    "    <h4 style=\"margin-top: 0; color: #0f766e; font-size: 16px; margin-bottom: 12px;\">Your Booking Summary:</h4>" +
                    "    <p style=\"margin: 6px 0; font-size: 14px;\"><strong style=\"color: #ffffff;\">Package:</strong> " + packageName + "</p>" +
                    "    <p style=\"margin: 6px 0; font-size: 14px;\"><strong style=\"color: #ffffff;\">Total Paid:</strong> LKR " + String.format("%,.2f", price) + "</p>" +
                    "    <p style=\"margin: 6px 0; font-size: 14px;\"><strong style=\"color: #ffffff;\">Status:</strong> Confirmed</p>" +
                    "  </div>" +

                    "  <hr style=\"border: 0; border-top: 1px solid #2c3034; margin-bottom: 20px;\">" +

                    // Footer Info
                    "  <div style=\"font-size: 13px; color: #6c757d; line-height: 1.6;\">" +
                    "    <p style=\"margin: 5px 0;\"><strong style=\"color: #adb5bd;\">Have questions?</strong> Check out our FAQ or visit our website: <a href=\"http://localhost:8080/\" style=\"color: #0f766e; text-decoration: none; font-weight: bold;\">TravelApp.com</a></p>" +
                    "    <p style=\"margin: 15px 0 5px 0; font-weight: bold; color: #adb5bd;\">Best regards,</p>" +
                    "    <p style=\"margin: 0; font-weight: bold; color: #ffffff;\">The TravelApp Team</p>" +
                    "  </div>" +

                    "</div>";

            // true කියන්නේ HTML එකක් විදිහට යවන්න කියන එක
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);
            System.out.println("📧 Beautiful HTML Booking Email Sent to: " + toEmail);

        } catch (Exception e) {
            System.out.println("❌ Failed to send HTML email: " + e.getMessage());
            e.printStackTrace();
        }
    }
}