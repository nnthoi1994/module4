package com.example.demo.service;

import com.example.demo.entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IBlogService{
    List<Blog> findAll();
    Page<Blog> findAll(Pageable pageable);
    Page<Blog> findAllByNameContaining(String searchName, Pageable pageable);
    Blog findById(int id);
    void save(Blog blog);
    void deleteById(int id);
    List<Blog> findByTitleContaining(String keyword);
}
