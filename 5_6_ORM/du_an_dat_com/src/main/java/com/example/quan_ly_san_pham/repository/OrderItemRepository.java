package com.example.quan_ly_san_pham.repository;

// Trong file OrderItemRepository.java


import com.example.quan_ly_san_pham.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrderId(Long orderId);
}
