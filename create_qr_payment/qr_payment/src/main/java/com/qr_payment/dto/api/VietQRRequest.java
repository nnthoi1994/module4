package com.qr_payment.dto.api;

// Dùng để tạo body gửi đến API VietQR
public class VietQRRequest {
    private String accountNo;
    private String accountName = "NGUYEN NGOC THOI"; // Không bắt buộc, có thể bỏ trống
    private int acqId;
    private int amount;
    private String addInfo;
    private String format = "text";
    private String template = "compact"; // "compact" sẽ trả về ảnh QR đẹp

    public VietQRRequest(String accountNo, int acqId, int amount, String addInfo) {
        this.accountNo = accountNo;
        this.acqId = acqId;
        this.amount = amount;
        this.addInfo = addInfo;
    }

    // Getters (Setter không cần thiết nếu dùng constructor)
    public String getAccountNo() { return accountNo; }
    public String getAccountName() { return accountName; }
    public int getAcqId() { return acqId; }
    public int getAmount() { return amount; }
    public String getAddInfo() { return addInfo; }
    public String getFormat() { return format; }
    public String getTemplate() { return template; }
}