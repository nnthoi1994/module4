package com.example.ordermeal.service;

import com.example.ordermeal.entity.Dish;
import com.example.ordermeal.entity.Order;
import com.example.ordermeal.entity.OrderItem;
import com.example.ordermeal.entity.User;
import com.example.ordermeal.repository.DishRepository;
import com.example.ordermeal.repository.OrderItemRepository;
import com.example.ordermeal.repository.OrderRepository;
import com.example.ordermeal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final DishRepository dishRepository;

    public Order getOrCreateCart(User user) {
        return orderRepository.findByUserIdAndOrderDateAndIsCompleted(user.getId(), LocalDate.now(), false)
                .orElseGet(() -> {
                    Order newCart = new Order();
                    newCart.setUser(user);
                    newCart.setOrderDate(LocalDate.now());
                    newCart.setTotalAmount(BigDecimal.ZERO);
                    newCart.setCompleted(false);
                    newCart.setItems(new ArrayList<>());
                    return orderRepository.save(newCart);
                });
    }

    public void addToCart(Long userId, Long dishId, int quantity) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Dish dish = dishRepository.findById(dishId).orElseThrow(() -> new RuntimeException("Dish not found"));
        Order cart = getOrCreateCart(user);

        Optional<OrderItem> existingItemOpt = cart.getItems().stream()
                .filter(item -> item.getDish().getId().equals(dishId))
                .findFirst();

        if (existingItemOpt.isPresent()) {
            OrderItem item = existingItemOpt.get();
            item.setQuantity(item.getQuantity() + quantity);
            orderItemRepository.save(item);
        } else {
            OrderItem newItem = new OrderItem();
            newItem.setOrder(cart);
            newItem.setDish(dish);
            newItem.setQuantity(quantity);
            newItem.setPricePerItem(dish.getPrice());
            cart.getItems().add(newItem);
            orderItemRepository.save(newItem);
        }

        updateCartTotal(cart);
    }

    private void updateCartTotal(Order cart) {
        BigDecimal total = cart.getItems().stream()
                .map(item -> item.getPricePerItem().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setTotalAmount(total);
        orderRepository.save(cart);
    }

    public void completeOrder(Long userId) {
        Order cart = getOrCreateCart(userRepository.findById(userId).orElseThrow());

        updateCartTotal(cart);

        cart.setCompleted(true);
        List<Order> otherCompletedOrders = orderRepository.findAllByUserIdAndOrderDateAndIsCompleted(userId, LocalDate.now(), true);
        if (!otherCompletedOrders.isEmpty()) {
            cart.setPaid(otherCompletedOrders.get(0).isPaid());
        }

        orderRepository.save(cart);
    }

    public void cancelOrder(Long userId) {
        Order cart = getOrCreateCart(userRepository.findById(userId).orElseThrow());
        orderRepository.delete(cart);
    }

    public List<Order> getTodaysCompletedOrders() {
        return orderRepository.findByOrderDateAndIsCompleted(LocalDate.now(), true);
    }

    // *** BẮT ĐẦU THÊM MỚI ***
    // Lấy các đơn hàng hôm nay ĐÃ HOÀN TẤT và CHƯA THANH TOÁN
    public List<Order> getTodaysCompletedAndUnpaidOrders() {
        return orderRepository.findByOrderDateAndIsCompletedAndIsPaid(LocalDate.now(), true, false);
    }
    // *** KẾT THÚC THÊM MỚI ***

    public List<Order> getPersonalHistory(Long userId) {
        return orderRepository.findByUserIdAndIsCompletedOrderByOrderDateDesc(userId, true);
    }

    public List<Order> getGeneralHistory() {
        return orderRepository.findAllByIsCompletedAndOrderDateBefore(true, LocalDate.now());
    }

    public void resetTodaysOrders() {
        List<Order> todaysOrders = orderRepository.findByOrderDate(LocalDate.now());
        orderRepository.deleteAll(todaysOrders);
    }

    @Transactional
    public void deleteCompletedOrderById(Long orderId) {
        orderRepository.deleteById(orderId);
    }

    @Transactional
    public void togglePaymentStatus(Long userId, LocalDate date) {
        List<Order> todaysOrders = orderRepository.findAllByUserIdAndOrderDateAndIsCompleted(userId, date, true);

        if (!todaysOrders.isEmpty()) {
            boolean newStatus = !todaysOrders.get(0).isPaid();
            for (Order order : todaysOrders) {
                order.setPaid(newStatus);
            }
            orderRepository.saveAll(todaysOrders);
        }
    }


    public List<User> getRandomUsersToFetchMeals() {
        List<Order> todaysOrders = getTodaysCompletedOrders();
        List<User> eligibleUsers = todaysOrders.stream()
                .map(Order::getUser)
                .filter(user -> "MALE".equalsIgnoreCase(user.getGender()) && !"ADMIN".equals(user.getRole()))
                .distinct()
                .collect(Collectors.toList());

        Collections.shuffle(eligibleUsers);
        return eligibleUsers.stream().limit(2).collect(Collectors.toList());
    }
}