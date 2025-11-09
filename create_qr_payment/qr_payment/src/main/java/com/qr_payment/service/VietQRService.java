package com.qr_payment.service; // Ghi chú: Đổi tên package này cho khớp với của bạn

import com.qr_payment.dto.api.VietQRRequest; // Đổi package cho khớp
import com.qr_payment.dto.api.VietQRResponse; // Đổi package cho khớp
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class VietQRService {

    private final RestTemplate restTemplate;
    private static final String API_URL = "https://api.vietqr.io/v2/generate";

    // ----- PHẦN THÊM VÀO -----
    // Thông tin tài khoản của BẠN
    // Bạn phải thay đổi thông tin này cho đúng với tài khoản nhận tiền
    private static final String YOUR_BIN = "970422"; // Ví dụ: BIN của MB Bank
    private static final String YOUR_ACCOUNT_NUMBER = "0935988224"; // STK của bạn
    // -------------------------

    @Autowired
    public VietQRService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * HÀM ĐÃ SỬA: Chỉ nhận 2 tham số
     */
    public String generateQRCode(int amount, String description) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Tự động dùng STK và BIN đã lưu ở trên
            VietQRRequest requestPayload = new VietQRRequest(
                    YOUR_ACCOUNT_NUMBER,
                    Integer.parseInt(YOUR_BIN),
                    amount,
                    description // Đây là nội dung TTDH PAY...
            );

            HttpEntity<VietQRRequest> entity = new HttpEntity<>(requestPayload, headers);
            VietQRResponse response = restTemplate.postForObject(API_URL, entity, VietQRResponse.class);

            if (response != null && "00".equals(response.getCode())) {
                return response.getQrDataUrl(); // Ảnh Base64
            } else {
                throw new RuntimeException("Lỗi từ API VietQR: " + (response != null ? response.getDesc() : "null"));
            }
        } catch (Exception e) {
            throw new RuntimeException("Không thể gọi API VietQR: " + e.getMessage());
        }
    }
}