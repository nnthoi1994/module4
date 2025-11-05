package com.example.demo.repository;

import com.example.demo.entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IBlogRepository extends JpaRepository<Blog, Integer> {
    List<Blog> findByTitleContaining(String keyword);
    Page<Blog> findAllByNameContaining(String searchName, Pageable pageable);
}
