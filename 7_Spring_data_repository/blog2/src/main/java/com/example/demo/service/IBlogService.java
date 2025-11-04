package com.example.demo.service;

import com.example.demo.entity.Blog;

import java.util.List;

public interface IBlogService{
    List<Blog> findAll();
    Blog findById(int id);
    void save(Blog blog);
    void deleteById(int id);
    List<Blog> findByTitleContaining(String keyword);
}
