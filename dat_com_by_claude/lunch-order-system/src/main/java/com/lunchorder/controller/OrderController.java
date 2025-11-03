package com.lunchorder.controller;

import com.lunchorder.model.User;
import com.lunchorder.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/add-to-cart")
    public String addToCart(@RequestParam Long dishId,
                            @RequestParam Integer quantity,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        try {
            User currentUser = (User) session.getAttribute("currentUser");
            orderService.addDishToCart(currentUser, dishId, quantity);
            redirectAttributes.addFlashAttribute("success", "Đã thêm món vào giỏ hàng");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/";
    }

    @PostMapping("/update-cart")
    public String updateCart(@RequestParam Long dishId,
                             @RequestParam Integer quantity,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        try {
            User currentUser = (User) session.getAttribute("currentUser");
            orderService.updateCartItem(currentUser, dishId, quantity);
            redirectAttributes.addFlashAttribute("success", "Đã cập nhật giỏ hàng");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/";
    }

    @PostMapping("/remove-from-cart")
    public String removeFromCart(@RequestParam Long dishId,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        try {
            User currentUser = (User) session.getAttribute("currentUser");
            orderService.removeFromCart(currentUser, dishId);
            redirectAttributes.addFlashAttribute("success", "Đã xóa món khỏi giỏ hàng");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/";
    }

    @PostMapping("/complete")
    public String completeOrder(HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            User currentUser = (User) session.getAttribute("currentUser");
            orderService.completeOrder(currentUser);
            redirectAttributes.addFlashAttribute("success", "Đã hoàn tất đơn hàng");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/";
    }

    @PostMapping("/cancel")
    public String cancelOrder(HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            User currentUser = (User) session.getAttribute("currentUser");
            orderService.cancelOrder(currentUser);
            redirectAttributes.addFlashAttribute("success", "Đã mở lại đơn hàng để chỉnh sửa");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/";
    }
}