package com.example.ordermeal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.ByteArrayResource; // *** THÊM IMPORT NÀY ***
import java.util.Base64; // *** THÊM IMPORT NÀY ***

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    // Giữ lại hàm cũ để không ảnh hưởng các chức năng khác
    public void sendEmail(String toEmail, String subject, String htmlContent) {
        // Gọi hàm mới với QR code là null
        sendEmail(toEmail, subject, htmlContent, null);
    }

    // *** HÀM MỚI ĐA NĂNG HƠN ***
    // (Thêm tham số qrCodeBase64)
    public void sendEmail(String toEmail, String subject, String htmlContent, String qrCodeBase64) {
        try {
            MimeMessage message = mailSender.createMimeMessage();

            // *** SỬA ĐỔI QUAN TRỌNG: true -> bật chế độ multipart ***
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            helper.setFrom("nnthoi1994@gmail.com");

            // *** BẮT ĐẦU LOGIC THÊM QR CODE ***
            if (qrCodeBase64 != null && !qrCodeBase64.isEmpty()) {
                try {
                    // 1. Tách chuỗi Base64
                    // Chuỗi có dạng: "data:image/png;base64,iVBORw0K..."
                    String[] parts = qrCodeBase64.split(",");
                    if (parts.length == 2) {
                        String base64Data = parts[1];

                        // 2. Giải mã chuỗi thành mảng byte
                        byte[] imageBytes = Base64.getDecoder().decode(base64Data);

                        // 3. Tạo nguồn tài nguyên từ mảng byte
                        ByteArrayResource imageResource = new ByteArrayResource(imageBytes);

                        // 4. Thêm tài nguyên vào email với CID (Content-ID) là "qrCodeImage"
                        // Tên "qrCodeImage" này sẽ được dùng trong thẻ <img>
                        helper.addInline("qrCodeImage", imageResource, "image/png");

                    } else {
                        System.err.println("❌ Lỗi định dạng Base64 QR code khi gửi mail.");
                    }
                } catch (Exception e) {
                    System.err.println("❌ Lỗi khi giải mã hoặc đính kèm QR code: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            // *** KẾT THÚC LOGIC THÊM QR CODE ***

            mailSender.send(message);
            System.out.println("✅ Email sent successfully to: " + toEmail);
        } catch (MessagingException e) {
            System.err.println("❌ Failed to send email to: " + toEmail);
            e.printStackTrace();
        }
    }
}