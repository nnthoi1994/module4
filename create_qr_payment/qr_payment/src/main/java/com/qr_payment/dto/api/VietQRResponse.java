package com.qr_payment.dto.api;

import java.util.Map;

// Dùng để hứng response (phản hồi) từ API VietQR
public class VietQRResponse {
    private String code;
    private String desc;
    private Map<String, Object> data;

    // Getters và Setters
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getDesc() { return desc; }
    public void setDesc(String desc) { this.desc = desc; }
    public Map<String, Object> getData() { return data; }
    public void setData(Map<String, Object> data) { this.data = data; }

    /**
     * Helper (hàm hỗ trợ) để lấy ảnh Base64
     * Tên trường trong 'data' là 'qrDataURL'
     */
    public String getQrDataUrl() {
        if (data != null && data.containsKey("qrDataURL")) {
            return (String) data.get("qrDataURL");
        }
        return null;
    }
}