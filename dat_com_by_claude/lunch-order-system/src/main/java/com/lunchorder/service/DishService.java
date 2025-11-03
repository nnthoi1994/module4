package com.lunchorder.service;

import com.lunchorder.model.Dish;
import com.lunchorder.repository.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DishService {

    @Autowired
    private DishRepository dishRepository;

    public Dish createDish(String name, Double price) {
        if (dishRepository.existsByName(name)) {
            throw new RuntimeException("Dish already exists");
        }

        Dish dish = new Dish(name, price);
        return dishRepository.save(dish);
    }

    public Optional<Dish> findById(Long id) {
        return dishRepository.findById(id);
    }

    public Optional<Dish> findByName(String name) {
        return dishRepository.findByName(name);
    }

    public List<Dish> findAll() {
        return dishRepository.findAllByOrderByNameAsc();
    }

    public Dish updateDish(Long id, String name, Double price) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dish not found"));
        dish.setName(name);
        dish.setPrice(price);
        return dishRepository.save(dish);
    }

    public void deleteDish(Long id) {
        dishRepository.deleteById(id);
    }
}