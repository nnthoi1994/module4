package com.bai_tap.service;

import com.bai_tap.entity.Blog;

import java.util.List;

public interface IBlogService {
    List<Blog> findAll();
    Blog findById(int id);
    boolean save(Blog blog);
    void deleteById(int id);
    List<Blog> searchByName(String keyword);
}
