package com.lunchorder.repository;

import com.lunchorder.model.MenuImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MenuImageRepository extends JpaRepository<MenuImage, Long> {

    List<MenuImage> findByUploadDate(LocalDate uploadDate);

    void deleteByUploadDate(LocalDate uploadDate);
}