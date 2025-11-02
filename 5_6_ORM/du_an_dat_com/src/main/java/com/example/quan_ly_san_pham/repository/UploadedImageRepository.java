package com.example.quan_ly_san_pham.repository;

import com.example.quan_ly_san_pham.entity.UploadedImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UploadedImageRepository extends JpaRepository<UploadedImage, Long> {

    // Lấy tất cả ảnh được upload SAU một thời điểm cụ thể.
    List<UploadedImage> findByUploadTimeAfter(LocalDateTime time);

    // Xóa tất cả ảnh được upload TRƯỚC một thời điểm cụ thể.
    void deleteByUploadTimeBefore(LocalDateTime time);

    // === THÊM DÒNG NÀY VÀO ===
    // Tìm tất cả ảnh được upload TRƯỚC một thời điểm cụ thể.
    List<UploadedImage> findAllByUploadTimeBefore(LocalDateTime time);
}
