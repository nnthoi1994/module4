package com.qr_payment.dto;

public class ConfirmPaymentRequest {
    private String transactionId;
    // Getters & Setters
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String id) { this.transactionId = id; }
}
