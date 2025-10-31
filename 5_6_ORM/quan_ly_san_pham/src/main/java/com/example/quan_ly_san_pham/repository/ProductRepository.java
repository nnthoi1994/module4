package com.example.quan_ly_san_pham.repository;

import com.example.quan_ly_san_pham.entity.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductRepository implements IProductRepository {
    private static final List<Product> products = new ArrayList<>();

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Product> findAll() {

        TypedQuery<Product> typedQuery = entityManager.createQuery("from Product ",Product.class);
        return typedQuery.getResultList();
    }

    @Override
    public Product findById(int id) {
        return entityManager.find(Product.class,id);

    }
    @Override
    @Transactional
    public boolean save(Product product) {
        try {
            if (product.getId() == 0) {
                entityManager.persist(product);
            } else {
                entityManager.merge(product);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    @Override
    @Transactional
    public void deleteById(int id) {
        Product product = entityManager.find(Product.class, id);
        if (product != null) {
            entityManager.remove(product);
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
