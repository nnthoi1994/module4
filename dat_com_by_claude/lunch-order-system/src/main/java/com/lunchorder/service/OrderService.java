package com.lunchorder.service;

import com.lunchorder.model.*;
import com.lunchorder.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private DishService dishService;

    @Autowired
    private OrderSettingService orderSettingService;

    public Order getOrCreateTodayOrder(User user) {
        LocalDate today = LocalDate.now();
        return orderRepository.findByUserAndOrderDate(user, today)
                .orElseGet(() -> {
                    Order order = new Order(user, today);
                    return orderRepository.save(order);
                });
    }

    public void addDishToCart(User user, Long dishId, Integer quantity) {
        Order order = getOrCreateTodayOrder(user);

        if (order.getStatus() == Order.OrderStatus.LOCKED) {
            throw new RuntimeException("Order is locked by admin");
        }

        Dish dish = dishService.findById(dishId)
                .orElseThrow(() -> new RuntimeException("Dish not found"));

        // Check if dish already in cart
        Optional<OrderItem> existingItem = order.getItems().stream()
                .filter(item -> item.getDish().getId().equals(dishId))
                .findFirst();

        if (existingItem.isPresent()) {
            OrderItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            OrderItem newItem = new OrderItem(order, dish, quantity);
            order.addItem(newItem);
        }

        order.calculateTotal();
        orderRepository.save(order);
    }

    public void updateCartItem(User user, Long dishId, Integer quantity) {
        Order order = getOrCreateTodayOrder(user);

        if (order.getStatus() == Order.OrderStatus.LOCKED) {
            throw new RuntimeException("Order is locked by admin");
        }

        OrderItem item = order.getItems().stream()
                .filter(i -> i.getDish().getId().equals(dishId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item not found in cart"));

        if (quantity <= 0) {
            order.removeItem(item);
        } else {
            item.setQuantity(quantity);
            order.calculateTotal();
        }

        orderRepository.save(order);
    }

    public void removeFromCart(User user, Long dishId) {
        Order order = getOrCreateTodayOrder(user);

        if (order.getStatus() == Order.OrderStatus.LOCKED) {
            throw new RuntimeException("Order is locked by admin");
        }

        OrderItem item = order.getItems().stream()
                .filter(i -> i.getDish().getId().equals(dishId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item not found in cart"));

        order.removeItem(item);
        orderRepository.save(order);
    }

    public void completeOrder(User user) {
        Order order = getOrCreateTodayOrder(user);

        if (order.getStatus() == Order.OrderStatus.LOCKED) {
            throw new RuntimeException("Order is locked by admin");
        }

        if (order.getItems().isEmpty()) {
            throw new RuntimeException("Cannot complete empty order");
        }

        order.setStatus(Order.OrderStatus.COMPLETED);
        order.setCompletedAt(LocalDateTime.now());
        orderRepository.save(order);
    }

    public void cancelOrder(User user) {
        Order order = getOrCreateTodayOrder(user);

        OrderSetting setting = orderSettingService.getTodaySetting();
        if (setting.getIsLocked()) {
            throw new RuntimeException("Cannot cancel order after admin locked");
        }

        order.setStatus(Order.OrderStatus.IN_CART);
        order.setCompletedAt(null);
        orderRepository.save(order);
    }

    public List<Order> getTodayCompletedOrders() {
        LocalDate today = LocalDate.now();
        return orderRepository.findByOrderDateAndStatus(today, Order.OrderStatus.COMPLETED);
    }

    public List<Order> getAllTodayOrders() {
        LocalDate today = LocalDate.now();
        return orderRepository.findByOrderDate(today);
    }

    public Map<Dish, Map<String, Object>> getTodaySummary() {
        List<Order> completedOrders = getTodayCompletedOrders();
        Map<Dish, Map<String, Object>> summary = new LinkedHashMap<>();

        for (Order order : completedOrders) {
            for (OrderItem item : order.getItems()) {
                Dish dish = item.getDish();
                summary.putIfAbsent(dish, new HashMap<>());
                Map<String, Object> dishData = summary.get(dish);

                int currentQty = (int) dishData.getOrDefault("quantity", 0);
                double currentTotal = (double) dishData.getOrDefault("total", 0.0);
                List<String> users = (List<String>) dishData.getOrDefault("users", new ArrayList<>());

                dishData.put("quantity", currentQty + item.getQuantity());
                dishData.put("total", currentTotal + item.getSubtotal());
                users.add(order.getUser().getFullName());
                dishData.put("users", users);
            }
        }

        return summary;
    }

    public Double getTodayTotalAmount() {
        return getTodayCompletedOrders().stream()
                .mapToDouble(Order::getTotalAmount)
                .sum();
    }

    public List<Order> getUserOrderHistory(User user) {
        return orderRepository.findByUserOrderByOrderDateDesc(user);
    }

    public Map<LocalDate, List<Order>> getAllOrderHistory() {
        List<LocalDate> dates = orderRepository.findDistinctOrderDates();
        Map<LocalDate, List<Order>> history = new LinkedHashMap<>();

        for (LocalDate date : dates) {
            List<Order> orders = orderRepository.findCompletedOrdersByDate(date);
            if (!orders.isEmpty()) {
                history.put(date, orders);
            }
        }

        return history;
    }

    public void resetTodayOrders() {
        LocalDate today = LocalDate.now();
        orderRepository.deleteByOrderDate(today);
    }
}