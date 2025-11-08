package com.example.ordermeal.dto;

// Dùng để tạo body gửi đến API VietQR
public class VietQRRequest {
    private String accountNo;
    private String accountName; // *** XÓA HARD-CODE ***
    private int acqId;
    private int amount;
    private String addInfo;
    private String format = "text";
    private String template = "compact";

    // *** SỬA CONSTRUCTOR ĐỂ NHẬN ACCOUNTNAME ***
    public VietQRRequest(String accountNo, String accountName, int acqId, int amount, String addInfo) {
        this.accountNo = accountNo;
        this.accountName = accountName; // *** THÊM DÒNG NÀY ***
        this.acqId = acqId;
        this.amount = amount;
        this.addInfo = addInfo;
    }

    // Getters
    public String getAccountNo() { return accountNo; }
    public String getAccountName() { return accountName; }
    public int getAcqId() { return acqId; }
    public int getAmount() { return amount; }
    public String getAddInfo() { return addInfo; }
    public String getFormat() { return format; }
    public String getTemplate() { return template; }
}