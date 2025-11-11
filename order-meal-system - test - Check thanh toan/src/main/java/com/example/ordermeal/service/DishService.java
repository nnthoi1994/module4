package com.example.ordermeal.service;

import com.example.ordermeal.entity.Dish;
import com.example.ordermeal.repository.DishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DishService {
    private final DishRepository dishRepository;

    public List<Dish> findAll() {
        // Sắp xếp giảm dần theo ID để món mới nhất hiện lên đầu
        return dishRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public Optional<Dish> findById(Long id) {
        return dishRepository.findById(id);
    }

    public Dish save(Dish dish) {
        if (dishRepository.findByName(dish.getName()).isEmpty()) {
            return dishRepository.save(dish);
        }
        return null;
    }

    public void deleteById(Long id) {
        try {
            dishRepository.deleteById(id);
        } catch (Exception e) {
            System.err.println("Lỗi khi xóa món ăn: " + e.getMessage());
        }
    }
}