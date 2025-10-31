package com.example.quan_ly_san_pham.service;

import com.example.quan_ly_san_pham.entity.Product;

import java.util.List;

public interface IProductService {
    List<Product> findAll();
    Product findById(int id);
    void save(Product product);
    void deleteById(int id);
    List<Product> searchByName(String keyword);
}
