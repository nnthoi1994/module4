package com.example.quan_ly_san_pham.controller;


import com.example.quan_ly_san_pham.entity.Order;
import com.example.quan_ly_san_pham.entity.User;
import com.example.quan_ly_san_pham.service.AppStateService;
import com.example.quan_ly_san_pham.service.EmailService;
import com.example.quan_ly_san_pham.service.OrderService;
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
@RequestMapping("/admin") // All URLs in this controller start with /admin
public class AdminController {

    @Autowired private AppStateService appStateService;
    @Autowired private OrderService orderService;
    @Autowired private EmailService emailService;

    /**
     * Helper method to check if the current user is an admin.
     * @param session The current HttpSession.
     * @return true if the user is an admin, false otherwise.
     */
    private boolean isAdmin(HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        return loggedInUser != null && "ADMIN".equals(loggedInUser.getRole());
    }

    /**
     * Handles the action to lock or unlock the ordering functionality for customers.
     */
    @PostMapping("/toggle-lock")
    public String toggleOrderLock(HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) {
            return "redirect:/"; // If not admin, redirect to home
        }

        boolean isCurrentlyLocked = appStateService.isOrderingLocked();
        appStateService.setOrderingLocked(!isCurrentlyLocked); // Toggle the state

        String message = isCurrentlyLocked ? "Đã MỞ KHÓA chức năng đặt cơm." : "Đã KHÓA chức năng đặt cơm.";
        redirectAttributes.addFlashAttribute("successMessage", message);
        return "redirect:/";
    }

    /**
     * Handles the action to reset all orders for the current day.
     */
    @PostMapping("/reset-orders")
    public String resetTodaysOrders(HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) {
            return "redirect:/";
        }

        orderService.resetTodaysOrders();
        redirectAttributes.addFlashAttribute("successMessage", "Đã reset toàn bộ đơn hàng hôm nay.");
        return "redirect:/";
    }

    /**
     * Handles the action to randomly select users to fetch meals.
     */
    @PostMapping("/random-picker")
    public String pickRandomUsers(HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) {
            return "redirect:/";
        }

        // Check if ordering is locked. This action should only be available after orders are finalized.
        if (!appStateService.isOrderingLocked()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng 'Kết thúc chọn món' trước khi chọn người đi lấy cơm.");
            return "redirect:/";
        }

        List<User> selectedUsers = orderService.getRandomUsersToFetchMeals();
        if (selectedUsers.isEmpty()) {
            redirectAttributes.addFlashAttribute("infoMessage", "Không có đủ người dùng nam hợp lệ để chọn.");
        } else {
            // Store the result in a flash attribute to be displayed on the home page.
            redirectAttributes.addFlashAttribute("randomPickerResult", selectedUsers);
            redirectAttributes.addFlashAttribute("successMessage", "Đã chọn người đi lấy cơm thành công!");
        }
        return "redirect:/";
    }

    /**
     * Handles the action to send payment notification emails to all customers who have completed their orders.
     */
    @PostMapping("/send-payment-emails")
    public String sendPaymentEmails(HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) {
            return "redirect:/";
        }

        List<Order> completedOrders = orderService.getTodaysCompletedOrders();
        if (completedOrders.isEmpty()) {
            redirectAttributes.addFlashAttribute("infoMessage", "Không có đơn hàng nào để gửi email thanh toán.");
            return "redirect:/";
        }

        // Use Locale for currency formatting
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        for (Order order : completedOrders) {
            User user = order.getUser();
            String totalAmountFormatted = currencyFormatter.format(order.getTotalAmount());

            // Build the email content using HTML
            String emailContent = String.format(
                    "<p>Chào %s,</p>" +
                            "<p>Hệ thống đặt cơm xin thông báo chi phí bữa trưa hôm nay của bạn.</p>" +
                            "<p><strong>Tổng số tiền cần thanh toán: %s</strong></p>" +
                            "<p>Cảm ơn bạn đã sử dụng dịch vụ!</p>",
                    user.getFullName(),
                    totalAmountFormatted
            );

            emailService.sendEmail(user.getEmail(), "Thông báo thanh toán tiền cơm", emailContent);
        }

        redirectAttributes.addFlashAttribute("successMessage", "Đã gửi email thanh toán đến " + completedOrders.size() + " người dùng.");
        return "redirect:/";
    }
}
