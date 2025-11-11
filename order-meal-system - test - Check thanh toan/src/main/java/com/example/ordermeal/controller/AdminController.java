package com.example.ordermeal.controller;

import com.example.ordermeal.entity.AppState;
import com.example.ordermeal.entity.Order;
import com.example.ordermeal.entity.User;
import com.example.ordermeal.service.AppStateService;
import com.example.ordermeal.service.EmailService;
import com.example.ordermeal.service.OrderService;
import com.example.ordermeal.service.VietQRService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AppStateService appStateService;
    private final OrderService orderService;
    private final EmailService emailService;
    private final VietQRService vietQRService;

    private boolean isAdmin(HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        return loggedInUser != null && "ADMIN".equals(loggedInUser.getRole());
    }

    @PostMapping("/update-bank-info")
    public String updateBankInfo(@RequestParam String bankBin,
                                 @RequestParam String bankAccountNo,
                                 @RequestParam String bankAccountName,
                                 @RequestParam String bankName,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không có quyền.");
            return "redirect:/";
        }
        appStateService.updateBankInfo(bankBin, bankAccountNo, bankAccountName, bankName);
        redirectAttributes.addFlashAttribute("successMessage", "Đã cập nhật thông tin thanh toán!");
        return "redirect:/";
    }

    @PostMapping("/toggle-lock")
    public String toggleOrderLock(HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) {
            return "redirect:/";
        }
        boolean isCurrentlyLocked = appStateService.isOrderingLocked();
        appStateService.setOrderingLocked(!isCurrentlyLocked);
        if (isCurrentlyLocked) {
            appStateService.resetPickerState();
        }
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
        appStateService.resetPickerState();
        redirectAttributes.addFlashAttribute("successMessage", "Đã reset toàn bộ đơn hàng hôm nay.");
        return "redirect:/";
    }

    @PostMapping("/send-payment-emails")
    public String sendPaymentEmails(HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) {
            return "redirect:/";
        }

        AppState appState = appStateService.getAppState();
        if (appState.getBankBin() == null || appState.getBankBin().isEmpty() || appState.getBankAccountNo() == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng 'Cài Đặt Thanh Toán (Admin)' trước khi gửi email.");
            return "redirect:/";
        }

        List<Order> unpaidOrders = orderService.getTodaysCompletedAndUnpaidOrders();

        if (unpaidOrders.isEmpty()) {
            redirectAttributes.addFlashAttribute("infoMessage", "Không có đơn hàng nào CHƯA THANH TOÁN để gửi email.");
            return "redirect:/";
        }

        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        List<User> usersToSend = unpaidOrders.stream()
                .map(Order::getUser)
                .distinct()
                .collect(Collectors.toList());

        for (User user : usersToSend) {
            List<Order> userUnpaidOrders = unpaidOrders.stream()
                    .filter(order -> order.getUser().getId().equals(user.getId()))
                    .collect(Collectors.toList());

            BigDecimal totalUnpaidAmount = userUnpaidOrders.stream()
                    .map(Order::getTotalAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            String totalAmountFormatted = currencyFormatter.format(totalUnpaidAmount);
            int amountInt = totalUnpaidAmount.intValue();

            String description = null;
            Optional<Order> firstOrder = userUnpaidOrders.stream().findFirst();
            if (firstOrder.isPresent()) {
                description = firstOrder.get().getPaymentCode();
            }

            String qrCodeBase64 = null;
            try {
                qrCodeBase64 = vietQRService.generateQRCode(
                        appState.getBankBin(),
                        appState.getBankAccountNo(),
                        appState.getBankAccountName(),
                        amountInt,
                        description
                );
            } catch (Exception e) {
                e.printStackTrace();
            }

            String emailContent = String.format(
                    "<p>Chào %s,</p>" +
                            "<p>Hệ thống đặt cơm xin thông báo chi phí bữa trưa hôm nay của bạn.</p>" +
                            "<p><strong>Tổng số tiền cần thanh toán: %s</strong></p>",
                    user.getFullName(),
                    totalAmountFormatted
            );

            if (qrCodeBase64 != null) {
                emailContent += String.format(
                        "<p>Bạn có thể quét mã QR dưới đây để thanh toán:</p>" +
                                "<div style='text-align:center;'>" +
                                "  <img src='cid:qrCodeImage' alt='Mã QR Thanh toán' style='width:250px; height:auto;'/>" +
                                "  <p><strong>Nội dung:</strong> %s</p>" +
                                "</div>",
                        description
                );
            } else {
                emailContent += "<p>Không thể tạo mã QR, vui lòng chuyển khoản thủ công.</p>";
            }

            emailContent += "<p>Cảm ơn bạn đã sử dụng dịch vụ!</p>";
            emailService.sendEmail(user.getEmail(), "Thông báo thanh toán tiền cơm", emailContent, qrCodeBase64);
        }

        redirectAttributes.addFlashAttribute("successMessage", "Đã gửi email thanh toán đến " + usersToSend.size() + " người dùng chưa thanh toán.");
        return "redirect:/";
    }

    @PostMapping("/order/delete/{orderId}")
    public String deleteCompletedOrder(@PathVariable Long orderId, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bạn không có quyền thực hiện hành động này.");
            return "redirect:/";
        }
        try {
            orderService.deleteCompletedOrderById(orderId);
            redirectAttributes.addFlashAttribute("successMessage", "Đã xóa đơn hàng thành công.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi xóa đơn hàng: " + e.getMessage());
        }
        return "redirect:/";
    }
}