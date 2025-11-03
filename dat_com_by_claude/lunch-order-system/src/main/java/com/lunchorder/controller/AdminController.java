package com.lunchorder.controller;

import com.lunchorder.model.Order;
import com.lunchorder.model.OrderItem;
import com.lunchorder.model.User;
import com.lunchorder.service.EmailService;
import com.lunchorder.service.OrderService;
import com.lunchorder.service.OrderSettingService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private OrderSettingService orderSettingService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/lock-ordering")
    public String lockOrdering(HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            User currentUser = (User) session.getAttribute("currentUser");
            if (currentUser.getRole() != User.Role.ADMIN) {
                redirectAttributes.addFlashAttribute("error", "Bạn không có quyền thực hiện thao tác này");
                return "redirect:/";
            }

            orderSettingService.lockOrdering();
            redirectAttributes.addFlashAttribute("success", "Đã khóa chức năng đặt món");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/";
    }

    @PostMapping("/unlock-ordering")
    public String unlockOrdering(HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            User currentUser = (User) session.getAttribute("currentUser");
            if (currentUser.getRole() != User.Role.ADMIN) {
                redirectAttributes.addFlashAttribute("error", "Bạn không có quyền thực hiện thao tác này");
                return "redirect:/";
            }

            orderSettingService.unlockOrdering();
            redirectAttributes.addFlashAttribute("success", "Đã mở lại chức năng đặt món");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/";
    }

    @PostMapping("/select-pickers")
    public String selectPickers(HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            User currentUser = (User) session.getAttribute("currentUser");
            if (currentUser.getRole() != User.Role.ADMIN) {
                redirectAttributes.addFlashAttribute("error", "Bạn không có quyền thực hiện thao tác này");
                return "redirect:/";
            }

            orderSettingService.selectRandomPickers();
            redirectAttributes.addFlashAttribute("success", "Đã chọn ngẫu nhiên 2 người đi lấy cơm");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/";
    }

    @PostMapping("/send-payment-emails")
    public String sendPaymentEmails(HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            User currentUser = (User) session.getAttribute("currentUser");
            if (currentUser.getRole() != User.Role.ADMIN) {
                redirectAttributes.addFlashAttribute("error", "Bạn không có quyền thực hiện thao tác này");
                return "redirect:/";
            }

            List<Order> completedOrders = orderService.getTodayCompletedOrders();
            int sentCount = 0;

            NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));

            for (Order order : completedOrders) {
                // Tạo chi tiết đơn hàng
                StringBuilder orderDetails = new StringBuilder();
                orderDetails.append("<ul>");
                for (OrderItem item : order.getItems()) {
                    orderDetails.append(String.format(
                            "<li><strong>%s</strong> - Số lượng: %d - Đơn giá: %s VNĐ - Thành tiền: %s VNĐ</li>",
                            item.getDish().getName(),
                            item.getQuantity(),
                            currencyFormat.format(item.getPrice()),
                            currencyFormat.format(item.getSubtotal())
                    ));
                }
                orderDetails.append("</ul>");

                // Gửi email
                emailService.sendPaymentNotification(
                        order.getUser().getEmail(),
                        order.getUser().getFullName(),
                        order.getTotalAmount(),
                        orderDetails.toString()
                );
                sentCount++;
            }

            redirectAttributes.addFlashAttribute("success",
                    "Đã gửi email thanh toán cho " + sentCount + " người");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/";
    }

    @PostMapping("/reset-orders")
    public String resetOrders(HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            User currentUser = (User) session.getAttribute("currentUser");
            if (currentUser.getRole() != User.Role.ADMIN) {
                redirectAttributes.addFlashAttribute("error", "Bạn không có quyền thực hiện thao tác này");
                return "redirect:/";
            }

            orderService.resetTodayOrders();
            redirectAttributes.addFlashAttribute("success", "Đã xóa toàn bộ đơn hàng hôm nay");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/";
    }
}