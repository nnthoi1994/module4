package com.example.ordermeal.controller;

import com.example.ordermeal.entity.Order;
import com.example.ordermeal.entity.User;
import com.example.ordermeal.service.AppStateService;
import com.example.ordermeal.service.EmailService;
import com.example.ordermeal.service.OrderService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AppStateService appStateService;
    private final OrderService orderService;
    private final EmailService emailService;

    private boolean isAdmin(HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        return loggedInUser != null && "ADMIN".equals(loggedInUser.getRole());
    }

    @PostMapping("/toggle-lock")
    public String toggleOrderLock(HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) {
            return "redirect:/";
        }

        boolean isCurrentlyLocked = appStateService.isOrderingLocked();
        appStateService.setOrderingLocked(!isCurrentlyLocked);

        String message = isCurrentlyLocked ? "Đã MỞ KHÓA chức năng đặt cơm." : "Đã KHÓA chức năng đặt cơm.";
        redirectAttributes.addFlashAttribute("successMessage", message);
        return "redirect:/";
    }

    @PostMapping("/reset-orders")
    public String resetTodaysOrders(HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) {
            return "redirect:/";
        }

        orderService.resetTodaysOrders();
        redirectAttributes.addFlashAttribute("successMessage", "Đã reset toàn bộ đơn hàng hôm nay.");
        return "redirect:/";
    }

    @PostMapping("/random-picker")
    public String pickRandomUsers(HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) {
            return "redirect:/";
        }

        if (!appStateService.isOrderingLocked()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng 'Kết thúc chọn món' trước khi chọn người đi lấy cơm.");
            return "redirect:/";
        }

        List<User> selectedUsers = orderService.getRandomUsersToFetchMeals();
        if (selectedUsers.isEmpty()) {
            redirectAttributes.addFlashAttribute("infoMessage", "Không có đủ người dùng nam hợp lệ để chọn.");
        } else {
            redirectAttributes.addFlashAttribute("randomPickerResult", selectedUsers);
            redirectAttributes.addFlashAttribute("successMessage", "Đã chọn người đi lấy cơm thành công!");
        }
        return "redirect:/";
    }

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

        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        for (Order order : completedOrders) {
            User user = order.getUser();
            String totalAmountFormatted = currencyFormatter.format(order.getTotalAmount());

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