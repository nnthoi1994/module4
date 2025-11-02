package com.example.quan_ly_san_pham.service;



import com.example.quan_ly_san_pham.entity.Dish;
import com.example.quan_ly_san_pham.entity.Order;
import com.example.quan_ly_san_pham.entity.OrderItem;
import com.example.quan_ly_san_pham.entity.User;
import com.example.quan_ly_san_pham.repository.DishRepository;
import com.example.quan_ly_san_pham.repository.OrderItemRepository;
import com.example.quan_ly_san_pham.repository.OrderRepository;
import com.example.quan_ly_san_pham.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional // Rất quan trọng: Đảm bảo tính toàn vẹn dữ liệu cho các thao tác phức tạp.
public class OrderService {

    @Autowired private OrderRepository orderRepository;
    @Autowired private OrderItemRepository orderItemRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private DishRepository dishRepository;

    /**
     * Lấy giỏ hàng (đơn hàng chưa hoàn tất) của người dùng trong ngày.
     * Nếu chưa có, một giỏ hàng mới sẽ được tự động tạo.
     * @param user Người dùng hiện tại.
     * @return Giỏ hàng của người dùng.
     */
    public Order getOrCreateCart(User user) {
        return orderRepository.findByUserIdAndOrderDateAndIsCompleted(user.getId(), LocalDate.now(), false)
                .orElseGet(() -> {
                    Order newCart = new Order();
                    newCart.setUser(user);
                    newCart.setOrderDate(LocalDate.now());
                    newCart.setTotalAmount(BigDecimal.ZERO);
                    newCart.setCompleted(false);
                    newCart.setItems(new ArrayList<>()); // Khởi tạo list rỗng
                    return orderRepository.save(newCart);
                });
    }

    /**
     * Thêm một món ăn với số lượng nhất định vào giỏ hàng của người dùng.
     * @param userId ID của người dùng.
     * @param dishId ID của món ăn.
     * @param quantity Số lượng.
     */
    public void addToCart(Long userId, Long dishId, int quantity) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Dish dish = dishRepository.findById(dishId).orElseThrow(() -> new RuntimeException("Dish not found"));
        Order cart = getOrCreateCart(user);

        // Kiểm tra xem món ăn đã có trong giỏ hàng chưa
        Optional<OrderItem> existingItemOpt = cart.getItems().stream()
                .filter(item -> item.getDish().getId().equals(dishId))
                .findFirst();

        if (existingItemOpt.isPresent()) {
            // Nếu có rồi, cập nhật số lượng
            OrderItem item = existingItemOpt.get();
            item.setQuantity(item.getQuantity() + quantity);
            orderItemRepository.save(item);
        } else {
            // Nếu chưa có, tạo một mục mới
            OrderItem newItem = new OrderItem();
            newItem.setOrder(cart);
            newItem.setDish(dish);
            newItem.setQuantity(quantity);
            newItem.setPricePerItem(dish.getPrice());
            cart.getItems().add(orderItemRepository.save(newItem));
        }

        updateCartTotal(cart);
    }

    /**
     * Cập nhật lại tổng tiền cho một giỏ hàng dựa trên các món và số lượng bên trong.
     * @param cart Giỏ hàng cần cập nhật.
     */
    private void updateCartTotal(Order cart) {
        // Tải lại các item từ DB để đảm bảo dữ liệu mới nhất
        cart.setItems(orderItemRepository.findByOrderId(cart.getId()));
        BigDecimal total = cart.getItems().stream()
                .map(item -> item.getPricePerItem().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setTotalAmount(total);
        orderRepository.save(cart);
    }

    /**
     * Đánh dấu giỏ hàng của người dùng là "đã hoàn tất".
     * @param userId ID của người dùng.
     */
    public void completeOrder(Long userId) {
        Order cart = getOrCreateCart(userRepository.findById(userId).orElseThrow());
        cart.setCompleted(true);
        orderRepository.save(cart);
    }

    /**
     * Hủy giỏ hàng hiện tại của người dùng (xóa hoàn toàn).
     * @param userId ID của người dùng.
     */
    public void cancelOrder(Long userId) {
        Order cart = getOrCreateCart(userRepository.findById(userId).orElseThrow());
        orderRepository.delete(cart);
    }

    /**
     * Lấy danh sách các đơn hàng đã được hoàn tất trong ngày hôm nay.
     * @return Danh sách các đơn hàng.
     */
    public List<Order> getTodaysCompletedOrders() {
        return orderRepository.findByOrderDateAndIsCompleted(LocalDate.now(), true);
    }

    /**
     * Lấy lịch sử đặt hàng cá nhân của một người dùng.
     * @param userId ID của người dùng.
     * @return Lịch sử các đơn hàng đã hoàn tất.
     */
    public List<Order> getPersonalHistory(Long userId) {
        return orderRepository.findByUserIdAndIsCompletedOrderByOrderDateDesc(userId, true);
    }

    /**
     * Lấy lịch sử đặt hàng chung của tất cả mọi người từ những ngày trước.
     * @return Lịch sử các đơn hàng đã hoàn tất.
     */
    public List<Order> getGeneralHistory() {
        return orderRepository.findAllByIsCompletedAndOrderDateBefore(true, LocalDate.now());
    }

    /**
     * (Admin) Xóa tất cả các đơn hàng (cả giỏ hàng và đơn đã hoàn tất) trong ngày hôm nay.
     */
    public void resetTodaysOrders() {
        List<Order> todaysOrders = orderRepository.findByOrderDate(LocalDate.now());
        orderRepository.deleteAll(todaysOrders);
    }

    /**
     * (Admin) Chọn ngẫu nhiên 2 người dùng nam đã đặt cơm để đi lấy cơm.
     * @return Danh sách 2 (hoặc ít hơn) người dùng được chọn.
     */
    public List<User> getRandomUsersToFetchMeals() {
        List<Order> todaysOrders = getTodaysCompletedOrders();
        List<User> eligibleUsers = todaysOrders.stream()
                .map(Order::getUser)
                .filter(user -> "MALE".equalsIgnoreCase(user.getGender()) && !"ADMIN".equals(user.getRole()))
                .distinct() // Đảm bảo mỗi người chỉ xuất hiện 1 lần dù có nhiều order
                .collect(Collectors.toList());

        Collections.shuffle(eligibleUsers); // Xáo trộn danh sách
        return eligibleUsers.stream().limit(2).collect(Collectors.toList()); // Lấy 2 người đầu tiên
    }
}
