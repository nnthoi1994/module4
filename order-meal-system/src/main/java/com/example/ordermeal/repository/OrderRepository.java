package com.example.ordermeal.repository;

import com.example.ordermeal.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByUserIdAndOrderDateAndIsCompleted(Long userId, LocalDate date, boolean isCompleted);
    List<Order> findByOrderDateAndIsCompleted(LocalDate date, boolean isCompleted);
    List<Order> findByUserIdAndIsCompletedOrderByOrderDateDesc(Long userId, boolean isCompleted);
    List<Order> findAllByIsCompletedAndOrderDateBefore(boolean isCompleted, LocalDate date);
    List<Order> findByOrderDate(LocalDate date);
}