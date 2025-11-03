package com.lunchorder.controller;

import com.lunchorder.model.*;
import com.lunchorder.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private DishService dishService;

    @Autowired
    private MenuImageService menuImageService;

    @Autowired
    private OrderSettingService orderSettingService;

    @GetMapping("/")
    public String home(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");

        // Lấy order hiện tại của user
        Order currentOrder = orderService.getOrCreateTodayOrder(currentUser);

        // Lấy danh sách món ăn
        List<Dish> dishes = dishService.findAll();

        // Lấy ảnh thực đơn hôm nay
        List<MenuImage> menuImages = menuImageService.getTodayImages();

        // Lấy tổng hợp đơn hàng
        Map<Dish, Map<String, Object>> summary = orderService.getTodaySummary();
        Double todayTotal = orderService.getTodayTotalAmount();

        // Lấy setting
        OrderSetting setting = orderSettingService.getTodaySetting();

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("currentOrder", currentOrder);
        model.addAttribute("dishes", dishes);
        model.addAttribute("menuImages", menuImages);
        model.addAttribute("summary", summary);
        model.addAttribute("todayTotal", todayTotal);
        model.addAttribute("setting", setting);
        model.addAttribute("isAdmin", currentUser.getRole() == User.Role.ADMIN);

        return "home";
    }
}