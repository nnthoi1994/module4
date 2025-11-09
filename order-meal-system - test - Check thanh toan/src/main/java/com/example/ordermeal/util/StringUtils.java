package com.example.ordermeal.util;

import java.text.Normalizer;
import java.security.SecureRandom;
import java.util.regex.Pattern;

public class StringUtils {

    private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * Chuyển chuỗi có dấu thành không dấu, viết hoa, không khoảng trắng.
     * Ví dụ: "Lê Văn Tám" -> "LEVANTAM"
     */
    public static String normalizeName(String name) {
        if (name == null) {
            return "";
        }
        // 1. Chuyển đổi Unicode (VD: 'á' -> 'a' + '´')
        String normalized = Normalizer.normalize(name, Normalizer.Form.NFD);

        // 2. Xóa các dấu (dấu '´')
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String noAccents = pattern.matcher(normalized).replaceAll("");

        // 3. Xóa ký tự 'Đ'/'đ'
        noAccents = noAccents.replaceAll("[đĐ]", "D");

        // 4. Xóa khoảng trắng và chuyển sang chữ hoa
        return noAccents.replaceAll("\\s+", "").toUpperCase();
    }

    /**
     * Tạo chuỗi ngẫu nhiên gồm chữ hoa và số.
     */
    public static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHARS.charAt(RANDOM.nextInt(CHARS.length())));
        }
        return sb.toString();
    }
}