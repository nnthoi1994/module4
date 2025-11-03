package com.lunchorder.service;

import com.lunchorder.model.Order;
import com.lunchorder.model.OrderSetting;
import com.lunchorder.model.User;
import com.lunchorder.repository.OrderRepository;
import com.lunchorder.repository.OrderSettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderSettingService {

    @Autowired
    private OrderSettingRepository orderSettingRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserService userService;

    public OrderSetting getTodaySetting() {
        LocalDate today = LocalDate.now();
        return orderSettingRepository.findBySettingDate(today)
                .orElseGet(() -> {
                    OrderSetting setting = new OrderSetting(today);
                    return orderSettingRepository.save(setting);
                });
    }

    public void lockOrdering() {
        OrderSetting setting = getTodaySetting();
        setting.setIsLocked(true);
        orderSettingRepository.save(setting);

        // Cập nhật tất cả đơn hàng thành LOCKED
        LocalDate today = LocalDate.now();
        List<Order> todayOrders = orderRepository.findByOrderDate(today);
        for (Order order : todayOrders) {
            if (order.getStatus() == Order.OrderStatus.COMPLETED) {
                order.setStatus(Order.OrderStatus.LOCKED);
                orderRepository.save(order);
            }
        }
    }

    public void unlockOrdering() {
        OrderSetting setting = getTodaySetting();
        setting.setIsLocked(false);
        orderSettingRepository.save(setting);

        // Cập nhật tất cả đơn hàng về COMPLETED
        LocalDate today = LocalDate.now();
        List<Order> todayOrders = orderRepository.findByOrderDate(today);
        for (Order order : todayOrders) {
            if (order.getStatus() == Order.OrderStatus.LOCKED) {
                order.setStatus(Order.OrderStatus.COMPLETED);
                orderRepository.save(order);
            }
        }
    }

    public void selectRandomPickers() {
        OrderSetting setting = getTodaySetting();

        if (!setting.getIsLocked()) {
            throw new RuntimeException("Phải khóa đặt món trước khi chọn người lấy cơm");
        }

        // Lấy danh sách nam giới đã hoàn tất đơn
        LocalDate today = LocalDate.now();
        List<Order> completedOrders = orderRepository.findByOrderDateAndStatus(
                today, Order.OrderStatus.LOCKED
        );

        List<User> maleUsers = completedOrders.stream()
                .map(Order::getUser)
                .filter(user -> user.getGender() == User.Gender.MALE)
                .distinct()
                .collect(Collectors.toList());

        if (maleUsers.size() < 2) {
            throw new RuntimeException("Không đủ khách hàng nam để chọn (cần ít nhất 2 người)");
        }

        // Chọn ngẫu nhiên 2 người
        Random random = new Random();
        User picker1 = maleUsers.get(random.nextInt(maleUsers.size()));
        maleUsers.remove(picker1);
        User picker2 = maleUsers.get(random.nextInt(maleUsers.size()));

        setting.setPicker1Username(picker1.getUsername());
        setting.setPicker2Username(picker2.getUsername());
        orderSettingRepository.save(setting);
    }

    public boolean isOrderingLocked() {
        return getTodaySetting().getIsLocked();
    }
}