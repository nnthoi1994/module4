package com.qr_payment.service;




import com.qr_payment.util.StringUtils;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TransactionService {

    // Dùng ConcurrentHashMap làm CSDL tạm (an toàn cho đa luồng)
    // Key: transactionId (ví dụ: PAY.NGUYENVANA.AXBYCZ)
    // Value: Trạng thái (PENDING hoặc PAID)
    private static final Map<String, String> transactionDatabase = new ConcurrentHashMap<>();

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom random = new SecureRandom();

    /**
     * Tạo một mã giao dịch mới và lưu vào "database"
     */
    public String createTransaction(String customerName) {
        String normalizedName = StringUtils.normalizeName(customerName);
        String uniqueCode = generateRandomString(6);
        String transactionId = String.format("PAY.%s.%s", normalizedName, uniqueCode);

        // Lưu với trạng thái PENDING
        transactionDatabase.put(transactionId, "PENDING");

        return transactionId;
    }

    /**
     * Kiểm tra trạng thái của một giao dịch
     */
    public String getTransactionStatus(String transactionId) {
        return transactionDatabase.getOrDefault(transactionId, "NOT_FOUND");
    }

    /**
     * Xác nhận một giao dịch (do Tasker gọi)
     */
    public boolean confirmTransaction(String transactionId) {
        // Kiểm tra xem giao dịch có tồn tại và đang PENDING không
        if ("PENDING".equals(transactionDatabase.get(transactionId))) {
            transactionDatabase.put(transactionId, "PAID");
            return true;
        }
        // Có thể giao dịch không tồn tại, hoặc đã được confirm rồi
        return false;
    }

    // Hàm tiện ích tạo chuỗi ngẫu nhiên
    private String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }
}
