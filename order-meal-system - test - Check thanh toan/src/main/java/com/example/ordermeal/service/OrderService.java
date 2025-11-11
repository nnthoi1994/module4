package com.example.ordermeal.service;

import com.example.ordermeal.entity.Dish;
import com.example.ordermeal.entity.Order;
import com.example.ordermeal.entity.OrderItem;
import com.example.ordermeal.entity.User;
import com.example.ordermeal.repository.DishRepository;
import com.example.ordermeal.repository.OrderItemRepository;
import com.example.ordermeal.repository.OrderRepository;
import com.example.ordermeal.repository.UserRepository;
import com.example.ordermeal.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    private final EmailService emailService;

    public Order getOrCreateCart(User user) {
        Order cart = orderRepository.findByUserIdAndOrderDateAndIsCompleted(user.getId(), LocalDate.now(), false)
                .orElseGet(() -> {
                    Order newCart = new Order();
                    newCart.setUser(user);
                    newCart.setOrderDate(LocalDate.now());
                    newCart.setTotalAmount(BigDecimal.ZERO);
                    newCart.setCompleted(false);
                    newCart.setPaid(false);
                    newCart.setItems(new ArrayList<>());
                    return newCart;
                });

        if (cart.getPaymentCode() == null || cart.getPaymentCode().isEmpty()) {
            cart.setPaymentCode(generatePaymentCode(user));
            cart = orderRepository.save(cart);
        }
        return cart;
    }

    private String generatePaymentCode(User user) {
        String normalizedName = StringUtils.normalizeName(user.getFullName());
        String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("ddMM"));
        String randomChars = StringUtils.generateRandomString(6);
        return "PAY" + normalizedName + dateStr + randomChars + "PAY";
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
            cart.getItems().add(orderItemRepository.save(newItem));
        }
        updateCartTotal(cart);

        if (cart.isPaid()) {
            cart.setPaid(false);
            cart.setPaymentCode(generatePaymentCode(user));
            orderRepository.save(cart);
        }
    }

    private void updateCartTotal(Order cart) {
        BigDecimal total = cart.getItems().stream()
                .map(item -> item.getPricePerItem().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalAmount(total);
        orderRepository.save(cart);
    }

    public void completeOrder(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        Order cart = getOrCreateCart(user);
        updateCartTotal(cart);

        if ("ADMIN".equals(user.getRole())) {
            cart.setCompleted(true);
            cart.setPaid(true);
            cart.setPaymentCode("ADMIN_COMPLETED");
            orderRepository.save(cart);
        } else {
            if (cart.getPaymentCode() == null) {
                cart.setPaymentCode(generatePaymentCode(user));
            }
            orderRepository.save(cart);
        }
    }

    public void cancelOrder(Long userId) {
        Order cart = getOrCreateCart(userRepository.findById(userId).orElseThrow());
        orderRepository.delete(cart);
    }

    // *** BẮT ĐẦU SỬA ĐỔI (HÀM CHECK MỚI) ***
    @Transactional(readOnly = true)
    public boolean checkPaymentStatusByCode(String paymentCode) {
        if (paymentCode == null || paymentCode.isEmpty()) return false;

        // Tìm đơn hàng có mã này
        Optional<Order> orderOpt = orderRepository.findFirstByPaymentCode(paymentCode);

        // Trả về true nếu tìm thấy VÀ đã thanh toán
        return orderOpt.map(Order::isPaid).orElse(false);
    }
    // *** KẾT THÚC SỬA ĐỔI ***

    @Transactional
    public boolean markAsPaidByPaymentCode(String paymentCode) {
        Optional<Order> orderOpt = orderRepository.findFirstByPaymentCode(paymentCode);

        if (orderOpt.isPresent()) {
            Order foundOrder = orderOpt.get();

            if (foundOrder.isCompleted()) {
                return false;
            }

            if (foundOrder.isPaid()) {
                return true;
            }

            User user = foundOrder.getUser();

            foundOrder.setPaid(true);
            foundOrder.setCompleted(true);
            orderRepository.save(foundOrder);

            try {
                BigDecimal totalAmount = foundOrder.getTotalAmount();
                NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                String totalAmountFormatted = currencyFormatter.format(totalAmount);

                String dishListHtml = foundOrder.getItems().stream()
                        .map(item -> String.format("<li>%s (SL: %d)</li>", item.getDish().getName(), item.getQuantity()))
                        .collect(Collectors.joining(""));

                String emailContent = String.format(
                        "<p>Chào %s,</p>" +
                                "<p>Hệ thống đã nhận được thanh toán. Đơn hàng của bạn đã được xác nhận.</p>" +
                                "<p><strong>Tổng tiền: %s</strong></p>" +
                                "<p>Món ăn:</p><ul>%s</ul>",
                        user.getFullName(),
                        totalAmountFormatted,
                        dishListHtml
                );
                emailService.sendEmail(user.getEmail(), "Xác nhận thanh toán thành công", emailContent);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    public List<Order> getTodaysCompletedOrders() {
        return orderRepository.findByOrderDateAndIsCompleted(LocalDate.now(), true);
    }
    public List<Order> getTodaysCompletedAndUnpaidOrders() {
        return orderRepository.findByOrderDateAndIsCompletedAndIsPaid(LocalDate.now(), true, false);
    }
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
    public void deleteCompletedOrderById(Long orderId) {
        orderRepository.deleteById(orderId);
    }
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