package com.lunchorder.repository;

import com.lunchorder.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {

    Optional<Dish> findByName(String name);

    List<Dish> findByPrice(Double price);

    boolean existsByName(String name);

    List<Dish> findAllByOrderByNameAsc();
}