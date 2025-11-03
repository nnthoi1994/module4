package com.lunchorder.repository;

import com.lunchorder.model.Order;
import com.lunchorder.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByUserAndOrderDate(User user, LocalDate orderDate);

    List<Order> findByOrderDate(LocalDate orderDate);

    List<Order> findByOrderDateAndStatus(LocalDate orderDate, Order.OrderStatus status);

    List<Order> findByUserOrderByOrderDateDesc(User user);

    @Query("SELECT DISTINCT o.orderDate FROM Order o ORDER BY o.orderDate DESC")
    List<LocalDate> findDistinctOrderDates();

    @Query("SELECT o FROM Order o WHERE o.orderDate = :date AND o.status = 'COMPLETED'")
    List<Order> findCompletedOrdersByDate(@Param("date") LocalDate date);

    void deleteByOrderDate(LocalDate orderDate);
}