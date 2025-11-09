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
        // ... (Không thay đổi)
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
        // ... (Không thay đổi)
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
    }

    private void updateCartTotal(Order cart) {
        // ... (Không thay đổi)
        BigDecimal total = cart.getItems().stream()
                .map(item -> item.getPricePerItem().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setTotalAmount(total);
        orderRepository.save(cart);
    }

    public void completeOrder(Long userId) {
        // ... (Không thay đổi)
        User user = userRepository.findById(userId).orElseThrow();
        Order cart = getOrCreateCart(user);

        updateCartTotal(cart);
        cart.setCompleted(true);

        LocalDate today = LocalDate.now();

        Optional<Order> existingOrder = orderRepository.findFirstByUserIdAndOrderDateAndIsCompletedOrderByOrderDateAsc(userId, today, true);

        String paymentCodeToUse;
        boolean isPaidStatus = false;

        if (existingOrder.isPresent() && existingOrder.get().getPaymentCode() != null) {
            paymentCodeToUse = existingOrder.get().getPaymentCode();
            isPaidStatus = existingOrder.get().isPaid();
        } else {
            String normalizedName = StringUtils.normalizeName(user.getFullName());
            String dateStr = today.format(DateTimeFormatter.ofPattern("ddMM"));
            String randomChars = StringUtils.generateRandomString(6);
            paymentCodeToUse = "PAY" + normalizedName + dateStr + randomChars + "PAY";
        }

        cart.setPaymentCode(paymentCodeToUse);
        cart.setPaid(isPaidStatus);

        orderRepository.save(cart);
    }

    public void cancelOrder(Long userId) {
        // ... (Không thay đổi)
        Order cart = getOrCreateCart(userRepository.findById(userId).orElseThrow());
        orderRepository.delete(cart);
    }

    public List<Order> getTodaysCompletedOrders() {
        // ... (Không thay đổi)
        return orderRepository.findByOrderDateAndIsCompleted(LocalDate.now(), true);
    }

    public List<Order> getTodaysCompletedAndUnpaidOrders() {
        // ... (Không thay đổi)
        return orderRepository.findByOrderDateAndIsCompletedAndIsPaid(LocalDate.now(), true, false);
    }

    public List<Order> getPersonalHistory(Long userId) {
        // ... (Không thay đổi)
        return orderRepository.findByUserIdAndIsCompletedOrderByOrderDateDesc(userId, true);
    }

    public List<Order> getGeneralHistory() {
        // ... (Không thay đổi)
        return orderRepository.findAllByIsCompletedAndOrderDateBefore(true, LocalDate.now());
    }

    public void resetTodaysOrders() {
        // ... (Không thay đổi)
        List<Order> todaysOrders = orderRepository.findByOrderDate(LocalDate.now());
        orderRepository.deleteAll(todaysOrders);
    }

    @Transactional
    public void deleteCompletedOrderById(Long orderId) {
        // ... (Không thay đổi)
        orderRepository.deleteById(orderId);
    }

    @Transactional
    public void togglePaymentStatus(Long userId, LocalDate date) {
        // ... (Không thay đổi)
        List<Order> todaysOrders = orderRepository.findAllByUserIdAndOrderDateAndIsCompleted(userId, date, true);

        if (!todaysOrders.isEmpty()) {
            boolean newStatus = !todaysOrders.get(0).isPaid();
            for (Order order : todaysOrders) {
                order.setPaid(newStatus);
            }
            orderRepository.saveAll(todaysOrders);
        }
    }

    // *** BẮT ĐẦU THÊM MỚI ***
    @Transactional(readOnly = true) // Chỉ đọc, không thay đổi
    public boolean checkTodayPaymentStatus(Long userId) {
        LocalDate today = LocalDate.now();
        // Tìm đơn hàng ĐÃ HOÀN TẤT đầu tiên của user
        Optional<Order> orderOpt = orderRepository.findFirstByUserIdAndOrderDateAndIsCompletedOrderByOrderDateAsc(userId, today, true);

        // Nếu họ có đơn hàng, trả về trạng thái isPaid
        // Nếu họ không có đơn hàng nào, họ cũng chưa paid -> trả về false
        return orderOpt.map(Order::isPaid).orElse(false);
    }
    // *** KẾT THÚC THÊM MỚI ***

    @Transactional
    public boolean markAsPaidByPaymentCode(String paymentCode) {
        // ... (Không thay đổi)
        Optional<Order> orderOpt = orderRepository.findFirstByPaymentCode(paymentCode);

        if (orderOpt.isPresent()) {
            Order foundOrder = orderOpt.get();

            if (foundOrder.isPaid()) {
                System.out.println("ℹ️ Webhook: Mã " + paymentCode + " đã được xử lý trước đó.");
                return true;
            }

            User user = foundOrder.getUser();
            LocalDate orderDate = foundOrder.getOrderDate();

            List<Order> allUserOrders = orderRepository.findAllByUserIdAndOrderDateAndIsCompleted(user.getId(), orderDate, true);

            for (Order order : allUserOrders) {
                order.setPaid(true);
            }
            orderRepository.saveAll(allUserOrders);

            try {
                BigDecimal totalAmount = allUserOrders.stream()
                        .map(Order::getTotalAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                String totalAmountFormatted = currencyFormatter.format(totalAmount);

                String dishListHtml = allUserOrders.stream()
                        .flatMap(o -> o.getItems().stream())
                        .collect(Collectors.groupingBy(
                                item -> item.getDish().getName(),
                                Collectors.summingInt(OrderItem::getQuantity)
                        ))
                        .entrySet().stream()
                        .map(entry -> String.format("<li>%s (SL: %d)</li>", entry.getKey(), entry.getValue()))
                        .collect(Collectors.joining(""));

                String emailContent = String.format(
                        "<p>Chào %s,</p>" +
                                "<p>Hệ thống vừa ghi nhận thanh toán thành công cho đơn hàng hôm nay của bạn.</p>" +
                                "<p><strong>Tổng số tiền đã thanh toán: %s</strong></p>" +
                                "<p>Các món ăn bao gồm:</p>" +
                                "<ul>%s</ul>" +
                                "<p>Cảm ơn bạn!</p>",
                        user.getFullName(),
                        totalAmountFormatted,
                        dishListHtml
                );

                emailService.sendEmail(user.getEmail(), "Xác nhận thanh toán thành công", emailContent);

            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("❌ Lỗi khi gửi email xác nhận thanh toán cho: " + user.getUsername());
            }

            return true;
        }

        return false;
    }


    public List<User> getRandomUsersToFetchMeals() {
        // ... (Không thay đổi)
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