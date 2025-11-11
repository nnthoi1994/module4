package com.example.ordermeal.controller;

import com.example.ordermeal.dto.PaymentWebhookRequest;
import com.example.ordermeal.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentWebhookController {

    private final OrderService orderService;

    /**
     * Đây là endpoint bí mật để Tasker của Admin gọi đến.
     * Cần thay đổi "mb-hook-a8f3j" thành một chuỗi bí mật của riêng bạn.
     */
    @PostMapping("/mb-hook-a8f3j")
    public ResponseEntity<?> handleMbBankWebhook(@RequestBody PaymentWebhookRequest request) {
        if (request == null || request.getPaymentCode() == null || request.getPaymentCode().isEmpty()) {
            return ResponseEntity.badRequest().body("Thiếu paymentCode");
        }

        try {
            boolean success = orderService.markAsPaidByPaymentCode(request.getPaymentCode());

            if (success) {
                System.out.println("✅ Webhook: Thanh toán thành công cho mã: " + request.getPaymentCode());
                return ResponseEntity.ok().body("Thanh toán được ghi nhận");
            } else {
                System.err.println("⚠️ Webhook: Không tìm thấy mã thanh toán: " + request.getPaymentCode());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy mã thanh toán");
            }
        } catch (Exception e) {
            System.err.println("❌ Webhook: Lỗi nghiêm trọng: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi máy chủ");
        }
    }
}