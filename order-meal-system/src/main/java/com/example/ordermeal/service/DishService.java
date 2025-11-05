package com.example.ordermeal.service;

import com.example.ordermeal.entity.Dish;
import com.example.ordermeal.repository.DishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DishService {
    private final DishRepository dishRepository;

    public List<Dish> findAll() {
        return dishRepository.findAll();
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

    // *** HÀM MỚI ***
    public void deleteById(Long id) {
        // (Chúng ta có thể thêm kiểm tra xem món ăn có tồn tại không,
        // nhưng hiện tại chỉ cần gọi xóa)
        try {
            dishRepository.deleteById(id);
        } catch (Exception e) {
            // Có thể log lỗi ở đây
            System.err.println("Lỗi khi xóa món ăn: " + e.getMessage());
        }
    }
}