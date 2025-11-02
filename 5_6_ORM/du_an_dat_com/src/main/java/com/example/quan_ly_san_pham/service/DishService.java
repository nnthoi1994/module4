package com.example.quan_ly_san_pham.service;

import com.example.quan_ly_san_pham.entity.Dish;
import com.example.quan_ly_san_pham.repository.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DishService {
    @Autowired
    private DishRepository dishRepository;

    public List<Dish> findAll() {
        return dishRepository.findAll();
    }

    public Optional<Dish> findById(Long id) {
        return dishRepository.findById(id);
    }

    public Dish save(Dish dish) {
        // Kiểm tra xem món ăn đã tồn tại chưa để tránh trùng lặp
        if (dishRepository.findByName(dish.getName()).isEmpty()) {
            return dishRepository.save(dish);
        }
        System.out.println("Món ăn với tên '" + dish.getName() + "' đã tồn tại.");
        return null; // Hoặc throw exception
    }
}
