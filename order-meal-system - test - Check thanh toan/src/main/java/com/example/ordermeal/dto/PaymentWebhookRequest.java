package com.example.ordermeal.dto;

import lombok.Data;

@Data // Tự động tạo Getters/Setters/Constructor
public class PaymentWebhookRequest {
    private String paymentCode;
}