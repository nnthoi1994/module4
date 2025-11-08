package com.qr_payment.dto;

// Lớp này dùng để ánh xạ JSON gửi từ JavaScript


public class GenerateQRRequest {
    private String customerName;
    private int amount;
    // Getters & Setters
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String n) { this.customerName = n; }
    public int getAmount() { return amount; }
    public void setAmount(int a) { this.amount = a; }
}