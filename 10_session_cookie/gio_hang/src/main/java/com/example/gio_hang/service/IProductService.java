package com.example.gio_hang.service;

import com.example.gio_hang.entity.Product;

import java.util.Optional;

public interface IProductService {
    Iterable<Product> findAll();

    Optional<Product> findById(Long id);
}
