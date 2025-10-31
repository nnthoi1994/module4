package com.example.quan_ly_san_pham.repository;

import com.example.quan_ly_san_pham.entity.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductRepository implements IProductRepository {
    private static final List<Product> products = new ArrayList<>();

    static {
        products.add(new Product(1, "iPhone 15", 12000000, "Flagship phone", "Apple"));
        products.add(new Product(2, "Galaxy S24", 11000000, "Premium Android", "Samsung"));
        products.add(new Product(3, "Xiaomi 14", 8000000, "Giá rẻ cấu hình mạnh", "Xiaomi"));
        products.add(new Product(4, "Xiaomi 15", 9000000, "Giá rẻ cấu hình mạnh", "Xiaomi"));
    }
    @Override
    public List<Product> findAll() {
        return products;
    }

    @Override
    public Product findById(int id) {
        for (Product p : products) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }
    @Override
    public void save(Product product) {
        if (product.getId() == 0) {
            int newId = 1;
            if (!products.isEmpty()) {
                Product lastProduct = products.get(products.size() - 1);
                newId = lastProduct.getId() + 1;
            }
            product.setId(newId);
            products.add(product);
        } else {
            for (int i = 0; i < products.size(); i++) {
                if (products.get(i).getId() == product.getId()) {
                    products.set(i, product);
                    return;
                }
            }
        }
    }
    @Override
    public void deleteById(int id) {
        int indexToRemove = -1;
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId() == id) {
                indexToRemove = i;
                break;
            }
        }
        if (indexToRemove != -1) {
            products.remove(indexToRemove);
        }
    }
    @Override
    public List<Product> searchByName(String keyword) {
        List<Product> result = new ArrayList<>();
        for (Product p : products) {
            if (p.getName().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(p);
            }
        }
        return result;
    }
}
