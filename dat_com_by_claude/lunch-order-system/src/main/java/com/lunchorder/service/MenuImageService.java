package com.lunchorder.service;

import com.lunchorder.model.MenuImage;
import com.lunchorder.model.User;
import com.lunchorder.repository.MenuImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class MenuImageService {

    @Autowired
    private MenuImageRepository menuImageRepository;

    private static final String UPLOAD_DIR = "uploads/menu-images/";

    public List<MenuImage> uploadImages(List<MultipartFile> files, User user) {
        List<MenuImage> uploadedImages = new ArrayList<>();
        LocalDate today = LocalDate.now();

        // Tạo thư mục nếu chưa có
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                continue;
            }

            try {
                // Tạo tên file unique
                String originalFilename = file.getOriginalFilename();
                String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                String uniqueFilename = UUID.randomUUID().toString() + extension;

                // Lưu file
                Path filePath = Paths.get(UPLOAD_DIR + uniqueFilename);
                Files.write(filePath, file.getBytes());

                // Lưu thông tin vào database
                MenuImage image = new MenuImage(
                        uniqueFilename,
                        filePath.toString(),
                        today,
                        user
                );
                uploadedImages.add(menuImageRepository.save(image));

            } catch (IOException e) {
                throw new RuntimeException("Failed to upload file: " + file.getOriginalFilename(), e);
            }
        }

        return uploadedImages;
    }

    public List<MenuImage> getTodayImages() {
        LocalDate today = LocalDate.now();
        return menuImageRepository.findByUploadDate(today);
    }

    public void deleteOldImages() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        List<MenuImage> oldImages = menuImageRepository.findByUploadDate(yesterday);

        for (MenuImage image : oldImages) {
            try {
                // Xóa file vật lý
                Path filePath = Paths.get(image.getFilePath());
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                System.err.println("Failed to delete file: " + image.getFilePath());
            }
        }

        // Xóa records trong database
        menuImageRepository.deleteByUploadDate(yesterday);
    }

    public void deleteImage(Long id) {
        MenuImage image = menuImageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        try {
            // Xóa file vật lý
            Path filePath = Paths.get(image.getFilePath());
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            System.err.println("Failed to delete file: " + image.getFilePath());
        }

        // Xóa record
        menuImageRepository.delete(image);
    }
}