package com.example.demo.service;

import com.example.demo.entity.Category;

import java.util.List;

public interface ICategoryService {
    List<Category> findAll();
    void save(Category category);
    void deleteById(Integer id);
    Category findById(Integer id);

}
