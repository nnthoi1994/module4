package com.example.quan_ly_san_pham.service;

import com.example.quan_ly_san_pham.entity.UploadedImage;
import com.example.quan_ly_san_pham.repository.UploadedImageRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ImageService {

    // !!! QUAN TRỌNG: Hãy thay đổi đường dẫn này thành một thư mục có thật trên máy của bạn.
    // Đây là nơi tất cả các ảnh upload sẽ được lưu trữ.
    private final String UPLOAD_DIR = "D:/meal_uploads/";

    @Autowired
    private UploadedImageRepository imageRepository;

    /**
     * Lưu file ảnh được tải lên vào thư mục đã định cấu hình và lưu đường dẫn vào database.
     * @param file Đối tượng MultipartFile từ form upload.
     * @return Đối tượng UploadedImage đã được lưu vào database.
     * @throws IOException nếu có lỗi khi ghi file.
     */
    public UploadedImage saveImage(MultipartFile file) throws IOException {
        // Tạo thư mục nếu nó chưa tồn tại để tránh lỗi.
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // Tạo một tên file ngẫu nhiên và duy nhất bằng cách kết hợp UUID với tên file gốc.
        // Điều này ngăn chặn việc ghi đè file nếu có người upload file trùng tên.
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(UPLOAD_DIR, fileName);

        // Ghi dữ liệu của file vào đường dẫn đã tạo.
        Files.write(filePath, file.getBytes());

        // Tạo đối tượng Entity để lưu vào database.
        UploadedImage image = new UploadedImage();
        // Chúng ta chỉ lưu đường dẫn web-accessible (/uploads/...) vào DB.
        // Đường dẫn này sẽ được dùng trong thẻ <img> của HTML.
        image.setFilePath("/uploads/" + fileName);
        image.setUploadTime(LocalDateTime.now());

        return imageRepository.save(image);
    }

    /**
     * Lấy danh sách tất cả các ảnh đã được upload trong ngày hôm nay.
     * @return Danh sách các đối tượng UploadedImage.
     */
    public List<UploadedImage> getTodaysImages() {
        // Lấy thời điểm bắt đầu của ngày hôm nay (00:00:00).
        LocalDateTime startOfToday = LocalDate.now().atStartOfDay();
        return imageRepository.findByUploadTimeAfter(startOfToday);
    }

    /**
     * Xóa tất cả các ảnh (cả file vật lý và record trong DB) đã được upload trước ngày hôm nay.
     * Chức năng này nên được gọi bởi một tác vụ định kỳ (Scheduled Task) để tự động dọn dẹp hàng ngày.
     */
    @Transactional // Đảm bảo tất cả các thao tác đều thành công, hoặc sẽ rollback nếu có lỗi.
    public void deleteOldImages() {
        LocalDateTime startOfToday = LocalDate.now().atStartOfDay();
        List<UploadedImage> oldImages = imageRepository.findAllByUploadTimeBefore(startOfToday);

        for (UploadedImage image : oldImages) {
            try {
                // Xóa file vật lý khỏi server.
                // Chúng ta cần lấy lại tên file gốc từ đường dẫn đã lưu trong DB.
                String fileNameFromDb = image.getFilePath().replace("/uploads/", "");
                Path path = Paths.get(UPLOAD_DIR, fileNameFromDb);
                Files.deleteIfExists(path);
            } catch (IOException e) {
                // Ghi lại lỗi nhưng không dừng tiến trình để có thể xóa các file khác.
                System.err.println("Không thể xóa file: " + image.getFilePath() + ". Lỗi: " + e.getMessage());
            }
        }

        // Sau khi đã cố gắng xóa các file vật lý, tiến hành xóa các record trong DB.
        imageRepository.deleteAll(oldImages);
    }
}
