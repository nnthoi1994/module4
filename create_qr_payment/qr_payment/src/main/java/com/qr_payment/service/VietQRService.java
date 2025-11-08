package com.qr_payment.service;

import com.qr_payment.dto.api.VietQRRequest;
import com.qr_payment.dto.api.VietQRResponse;
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

    @Autowired
    public VietQRService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Gọi API VietQR để tạo mã QR
     * @return Chuỗi Base64 của ảnh QR
     */
    public String generateQRCode(String bin, String accountNumber, int amount, String description) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // acqId chính là BIN
            VietQRRequest requestPayload = new VietQRRequest(
                    accountNumber,
                    Integer.parseInt(bin),
                    amount,
                    description
            );

            HttpEntity<VietQRRequest> entity = new HttpEntity<>(requestPayload, headers);
            VietQRResponse response = restTemplate.postForObject(API_URL, entity, VietQRResponse.class);

            if (response != null && "00".equals(response.getCode())) {
                // Trả về chuỗi Base64 (data:image/png;base64,...)
                return response.getQrDataUrl();
            } else {
                String error = (response != null) ? response.getDesc() : "Response rỗng";
                throw new RuntimeException("Lỗi từ API VietQR: " + error);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Không thể gọi API VietQR: " + e.getMessage());
        }
    }
}