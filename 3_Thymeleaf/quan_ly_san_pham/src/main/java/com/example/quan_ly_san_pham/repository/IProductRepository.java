package com.example.quan_ly_san_pham.repository;

import com.example.quan_ly_san_pham.entity.Product;

import java.util.List;

public interface IProductRepository {
    List<Product> findAll();
    Product findById(int id);
    void save(Product product);
    void deleteById(int id);
    List<Product> searchByName(String keyword);
}
