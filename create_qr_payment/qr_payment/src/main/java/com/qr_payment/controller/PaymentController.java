package com.qr_payment.controller;

import com.qr_payment.dto.ConfirmPaymentRequest;
import com.qr_payment.dto.GenerateQRRequest;
import com.qr_payment.service.TransactionService;
import com.qr_payment.service.VietQRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class PaymentController {

    // API Key bí mật của bạn
    private static final String API_KEY = "01665911354";

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private VietQRService vietQRService;

    /**
     * Endpoint 1: Frontend gọi để TẠO MÃ QR
     */
    @PostMapping("/generate-qr")
    public ResponseEntity<?> generateQR(@RequestBody GenerateQRRequest request) {
        try {
            // 1. Tạo mã giao dịch (PAY.NGUYENVANA.AXBYCZ)
            String transactionId = transactionService.createTransaction(request.getCustomerName());

            // 2. Nội dung thanh toán (sẽ hiển thị trong app ngân hàng)
            String qrDescription = "TTDH " + transactionId;

            // 3. Gọi VietQR API để lấy ảnh
            String base64Image = vietQRService.generateQRCode(request.getAmount(), qrDescription);

            // 4. Trả về ảnh và mã giao dịch cho Frontend
            return ResponseEntity.ok(Map.of(
                    "qrImage", base64Image,
                    "transactionId", transactionId
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Endpoint 2: Frontend gọi để "HỎI THĂM" (Polling)
     */
    @GetMapping("/check-status/{transactionId}")
    public ResponseEntity<?> checkStatus(@PathVariable String transactionId) {
        String status = transactionService.getTransactionStatus(transactionId);
        return ResponseEntity.ok(Map.of("status", status));
    }

    /**
     * Endpoint 3: TASKER gọi để XÁC NHẬN THANH TOÁN
     */
    @PostMapping("/confirm-payment")
    public ResponseEntity<?> confirmPayment(
            @RequestHeader(value = "X-API-Key", required = false) String apiKey,
            @RequestBody ConfirmPaymentRequest request) {

        // 1. Kiểm tra API Key
        if (!API_KEY.equals(apiKey)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Sai API Key"));
        }

        // 2. Lấy mã giao dịch từ Tasker
        String transactionId = request.getTransactionId();
        if (transactionId == null || transactionId.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Không có transactionId"));
        }

        // 3. Cập nhật trạng thái
        boolean success = transactionService.confirmTransaction(transactionId);

        if (success) {
            return ResponseEntity.ok(Map.of("message", "Xác nhận thành công: " + transactionId));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Mã giao dịch không tồn tại hoặc đã được xử lý"));
        }
    }
}