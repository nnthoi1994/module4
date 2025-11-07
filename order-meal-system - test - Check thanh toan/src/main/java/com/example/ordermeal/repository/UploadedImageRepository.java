package com.example.ordermeal.repository;

import com.example.ordermeal.entity.UploadedImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UploadedImageRepository extends JpaRepository<UploadedImage, Long> {
    List<UploadedImage> findByUploadTimeAfter(LocalDateTime time);
    List<UploadedImage> findAllByUploadTimeBefore(LocalDateTime time);
}