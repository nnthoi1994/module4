package com.example.quan_ly_san_pham.service;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Service;
import java.util.Properties;

@Service
public class EmailService {
    // !!! QUAN TRỌNG: Thay thế bằng thông tin email và Mật khẩu ứng dụng của bạn
    // Để tạo mật khẩu ứng dụng, vào tài khoản Google -> Bảo mật -> Xác minh 2 bước -> Mật khẩu ứng dụng.
    private static final String FROM_EMAIL = "nnthoi1994@gmail.com";
    private static final String FROM_PASSWORD = "lrzg edmh vfxe jxdl"; // Đây là Mật khẩu Ứng dụng

    /**
     * Gửi một email với nội dung HTML.
     * @param toEmail Địa chỉ email người nhận.
     * @param subject Tiêu đề của email.
     * @param htmlContent Nội dung email dưới dạng chuỗi HTML.
     */
    public void sendEmail(String toEmail, String subject, String htmlContent) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(FROM_EMAIL, FROM_PASSWORD);
                }
            });

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL, "Hệ Thống Đặt Cơm", "UTF-8"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject, "UTF-8");

            // Đặt content type là html để có thể định dạng email (in đậm, xuống dòng, etc.)
            message.setContent(htmlContent, "text/html; charset=utf-8");

            Transport.send(message);
            System.out.println("✅ Email sent successfully to: " + toEmail);
        } catch (Exception e) {
            System.err.println("❌ Failed to send email to: " + toEmail + ". Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
