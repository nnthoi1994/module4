package org.example.demo2.service;

import org.example.demo2.entity.Product;

import java.util.List;

public interface IProductService {
    List<Product> findAll();

    Product findById(int id);

    void save(Product product);

    void delete(int id);
}
