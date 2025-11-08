package com.qr_payment.util;



import java.text.Normalizer;
import java.util.regex.Pattern;

public class StringUtils {

    // Hàm chuẩn hóa tên: "Nguyễn Văn A 1" -> "NGUYENVANA1"
    public static String normalizeName(String name) {
        if (name == null || name.isEmpty()) {
            return "KHACHLE"; // Tên mặc định nếu rỗng
        }

        // 1. Chuyển sang không dấu
        String nfdNormalizedString = Normalizer.normalize(name, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String noDiacritics = pattern.matcher(nfdNormalizedString).replaceAll("");

        // 2. Bỏ ký tự đặc biệt, bỏ cách, chuyển hoa
        return noDiacritics
                .replaceAll("[^a-zA-Z0-9]", "") // Chỉ giữ lại chữ và số
                .toUpperCase();
    }
}
