package com.example.quan_ly_san_pham.repository;





import com.example.quan_ly_san_pham.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // === CÁC PHƯƠNG THỨC MỚI CẦN THÊM ===

    // Tìm một đơn hàng (hoặc giỏ hàng) của một user cụ thể, trong một ngày cụ thể, với trạng thái hoàn tất cụ thể.
    // Dùng để lấy giỏ hàng hiện tại của người dùng.
    Optional<Order> findByUserIdAndOrderDateAndIsCompleted(Long userId, LocalDate date, boolean isCompleted);

    // Tìm tất cả đơn hàng trong một ngày với trạng thái hoàn tất cụ thể.
    // Dùng để lấy danh sách tất cả người dùng đã đặt cơm xong trong ngày.
    List<Order> findByOrderDateAndIsCompleted(LocalDate date, boolean isCompleted);

    // Tìm tất cả các đơn hàng của một user, đã hoàn tất, sắp xếp theo ngày giảm dần.
    // Dùng cho chức năng xem lịch sử cá nhân.
    List<Order> findByUserIdAndIsCompletedOrderByOrderDateDesc(Long userId, boolean isCompleted);

    // Tìm tất cả các đơn hàng đã hoàn tất TRƯỚC ngày hôm nay.
    // Dùng cho chức năng xem lịch sử chung.
    List<Order> findAllByIsCompletedAndOrderDateBefore(boolean isCompleted, LocalDate date);

    // Tìm tất cả đơn hàng trong một ngày, không phân biệt trạng thái.
    // Dùng cho chức năng Reset của Admin.
    List<Order> findByOrderDate(LocalDate date);
}
