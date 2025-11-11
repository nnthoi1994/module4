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
    List<Order> findAllByUserIdAndOrderDateAndIsCompleted(Long userId, LocalDate date, boolean isCompleted);
    List<Order> findByOrderDateAndIsCompletedAndIsPaid(LocalDate date, boolean isCompleted, boolean isPaid);

    // *** BẮT ĐẦU THÊM MỚI ***
    // 1. Dùng để tìm mã thanh toán đã có của user trong ngày
    Optional<Order> findFirstByUserIdAndOrderDateAndIsCompletedOrderByOrderDateAsc(Long userId, LocalDate date, boolean isCompleted);

    // 2. Dùng cho Webhook để tìm đơn hàng
    Optional<Order> findFirstByPaymentCode(String paymentCode);
    // *** KẾT THÚC THÊM MỚI ***
}