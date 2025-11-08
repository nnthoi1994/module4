package com.example.ordermeal.service;

import com.example.ordermeal.dto.VietQRRequest; // *** SỬA IMPORT ***
import com.example.ordermeal.dto.VietQRResponse; // *** SỬA IMPORT ***
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
    // *** SỬA THAM SỐ, THÊM accountName ***
    public String generateQRCode(String bin, String accountNumber, String accountName, int amount, String description) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // acqId chính là BIN
            VietQRRequest requestPayload = new VietQRRequest(
                    accountNumber,
                    accountName, // *** THÊM THAM SỐ NÀY ***
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