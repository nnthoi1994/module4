package org.example.demo2.repository;

import org.example.demo2.entity.Product;

import java.util.List;

public interface IProductRepository {
    List<Product> findAll();

    Product findById(int id);

    void save(Product product);


    void delete(int id);
}
