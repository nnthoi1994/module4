package com.example.ordermeal.service;

import com.example.ordermeal.entity.UploadedImage;
import com.example.ordermeal.repository.UploadedImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {
    @Value("${app.upload.path}")
    private String UPLOAD_DIR;

    private final UploadedImageRepository imageRepository;

    public UploadedImage saveImage(MultipartFile file) throws IOException {
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(UPLOAD_DIR, fileName);
        Files.write(filePath, file.getBytes());

        UploadedImage image = new UploadedImage();
        image.setFilePath("/uploads/" + fileName);
        image.setUploadTime(LocalDateTime.now());

        return imageRepository.save(image);
    }

    public List<UploadedImage> getTodaysImages() {
        LocalDateTime startOfToday = LocalDate.now().atStartOfDay();
        return imageRepository.findByUploadTimeAfter(startOfToday);
    }

    @Transactional
    public void deleteOldImages() {
        LocalDateTime startOfToday = LocalDate.now().atStartOfDay();
        List<UploadedImage> oldImages = imageRepository.findAllByUploadTimeBefore(startOfToday);

        for (UploadedImage image : oldImages) {
            try {
                // Tách tên file từ CSDL (e.g., /uploads/abc.jpg -> abc.jpg)
                String fileNameFromDb = image.getFilePath().replace("/uploads/", "");
                Path path = Paths.get(UPLOAD_DIR, fileNameFromDb);
                Files.deleteIfExists(path);
            } catch (IOException e) {
                System.err.println("Không thể xóa file vật lý: " + image.getFilePath());
            }
        }

        imageRepository.deleteAll(oldImages);
    }

    // *** HÀM MỚI ĐỂ XÓA ẢNH THEO ID ***
    @Transactional
    public void deleteImageById(Long id) {
        Optional<UploadedImage> imageOpt = imageRepository.findById(id);
        if (imageOpt.isPresent()) {
            UploadedImage image = imageOpt.get();
            try {
                // 1. Xóa file vật lý
                String fileNameFromDb = image.getFilePath().replace("/uploads/", "");
                Path physicalPath = Paths.get(UPLOAD_DIR, fileNameFromDb);
                Files.deleteIfExists(physicalPath);

                // 2. Xóa bản ghi trong database
                imageRepository.deleteById(id);

            } catch (IOException e) {
                System.err.println("Lỗi khi xóa file vật lý: " + image.getFilePath());
                // (Bạn có thể chọn ném một exception ở đây nếu muốn)
            } catch (Exception e) {
                System.err.println("Lỗi khi xóa ảnh khỏi CSDL: " + e.getMessage());
            }
        }
    }
}