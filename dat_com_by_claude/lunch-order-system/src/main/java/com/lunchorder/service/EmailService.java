package com.lunchorder.service;

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
    private static final String FROM_PASSWORD = "lrzg edmh vfxe jxdl"; // Mật khẩu Ứng dụng

    /**
     * Gửi một email với nội dung HTML.
     */
    public void sendEmail(String toEmail, String subject, String htmlContent) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.ssl.protocols", "TLSv1.2");

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
            message.setContent(htmlContent, "text/html; charset=utf-8");

            Transport.send(message);
            System.out.println("✅ Email sent successfully to: " + toEmail);
        } catch (Exception e) {
            System.err.println("❌ Failed to send email to: " + toEmail + ". Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Gửi email thông báo thanh toán cho người dùng
     */
    public void sendPaymentNotification(String toEmail, String userName, Double totalAmount, String orderDetails) {
        String subject = "Thông báo thanh toán đặt cơm - " + java.time.LocalDate.now();
        String htmlContent = buildPaymentEmailContent(userName, totalAmount, orderDetails);
        sendEmail(toEmail, subject, htmlContent);
    }

    /**
     * Xây dựng nội dung HTML cho email thanh toán
     */
    private String buildPaymentEmailContent(String userName, Double totalAmount, String orderDetails) {
        return String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; background-color: #f9f9f9; }
                    .header { background-color: #4CAF50; color: white; padding: 20px; text-align: center; border-radius: 5px 5px 0 0; }
                    .content { background-color: white; padding: 20px; border-radius: 0 0 5px 5px; }
                    .total { font-size: 24px; font-weight: bold; color: #4CAF50; margin: 20px 0; }
                    .details { background-color: #f5f5f5; padding: 15px; border-left: 4px solid #4CAF50; margin: 15px 0; }
                    .footer { text-align: center; margin-top: 20px; color: #777; font-size: 12px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>🍱 Thông Báo Thanh Toán</h1>
                    </div>
                    <div class="content">
                        <p>Xin chào <strong>%s</strong>,</p>
                        <p>Đây là thông báo thanh toán cho đơn đặt cơm của bạn hôm nay:</p>
                        
                        <div class="details">
                            <h3>Chi tiết đơn hàng:</h3>
                            %s
                        </div>
                        
                        <div class="total">
                            Tổng tiền cần thanh toán: %,.0f VNĐ
                        </div>
                        
                        <p>Vui lòng thanh toán cho người đi lấy cơm hoặc chuyển khoản theo thông tin đã cung cấp.</p>
                        <p>Xin cảm ơn!</p>
                    </div>
                    <div class="footer">
                        <p>Email này được gửi tự động từ Hệ Thống Đặt Cơm</p>
                        <p>Vui lòng không trả lời email này</p>
                    </div>
                </div>
            </body>
            </html>
            """, userName, orderDetails, totalAmount);
    }
}