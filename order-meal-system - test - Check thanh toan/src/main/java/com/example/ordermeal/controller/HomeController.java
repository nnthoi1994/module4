package com.example.ordermeal.controller;

import com.example.ordermeal.entity.*;
import com.example.ordermeal.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {

    // *** BẮT ĐẦU SỬA ĐỔI ***
    // Sửa đổi record UserSummary để giữ List<OrderItem> đã được gộp lại
    public record UserSummary(User user, List<OrderItem> mergedItems, int totalQuantity, BigDecimal totalAmount) {}
    // *** KẾT THÚC SỬA ĐỔI ***

    private final IUserService userService;
    private final ImageService imageService;
    private final DishService dishService;
    private final OrderService orderService;
    private final AppStateService appStateService;
    private final ObjectMapper objectMapper;

    @ModelAttribute
    public void addUserToModel(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            userService.findById(loggedInUser.getId()).ifPresent(user -> model.addAttribute("loggedInUser", user));
        }
    }

    @GetMapping
    public String homePage(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        // Add basic data
        model.addAttribute("todaysImages", imageService.getTodaysImages());
        model.addAttribute("dishes", dishService.findAll());

        // LẤY TRẠNG THÁI MỚI
        AppState appState = appStateService.getAppState();
        model.addAttribute("appState", appState);
        model.addAttribute("isOrderingLocked", appState.isOrderingLocked());
        model.addAttribute("selectedUsers", appStateService.getSelectedUsers());

        // Get cart
        Order cart = orderService.getOrCreateCart(loggedInUser);
        model.addAttribute("cart", cart);

        // Get today's completed orders
        List<Order> todaysCompletedOrders = orderService.getTodaysCompletedOrders();


        // Process summary data
        if (todaysCompletedOrders != null && !todaysCompletedOrders.isEmpty()) {
            List<OrderItem> allItems = todaysCompletedOrders.stream()
                    .filter(order -> order.getItems() != null)
                    .flatMap(order -> order.getItems().stream())
                    .collect(Collectors.toList());

            if (!allItems.isEmpty()) {
                Map<String, List<OrderItem>> groupedItems = allItems.stream()
                        .collect(Collectors.groupingBy(item -> item.getDish().getName()));

                Map<String, Integer> totalQuantities = groupedItems.entrySet().stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> entry.getValue().stream().mapToInt(OrderItem::getQuantity).sum()
                        ));

                Map<String, BigDecimal> subtotals = groupedItems.entrySet().stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> entry.getValue().stream()
                                        .map(item -> item.getPricePerItem().multiply(BigDecimal.valueOf(item.getQuantity())))
                                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        ));

                BigDecimal grandTotal = subtotals.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
                int summaryTotalQuantity = allItems.stream().mapToInt(OrderItem::getQuantity).sum();


                model.addAttribute("groupedItems", groupedItems);
                model.addAttribute("totalQuantities", totalQuantities);
                model.addAttribute("subtotals", subtotals);
                model.addAttribute("grandTotal", grandTotal); // Cho bảng 1

                model.addAttribute("summaryTotalAmount", grandTotal); // Dùng chung cho bảng 2
                model.addAttribute("summaryTotalQuantity", summaryTotalQuantity); // Cho bảng 2

            } else {
                model.addAttribute("groupedItems", Collections.emptyMap());
                model.addAttribute("totalQuantities", Collections.emptyMap());
                model.addAttribute("subtotals", Collections.emptyMap());
                model.addAttribute("grandTotal", BigDecimal.ZERO);

                model.addAttribute("summaryTotalAmount", BigDecimal.ZERO);
                model.addAttribute("summaryTotalQuantity", 0);
            }

            // *** BẮT ĐẦU SỬA ĐỔI (LOGIC GỘP NHÓM) ***
            Map<User, List<Order>> ordersByUser = todaysCompletedOrders.stream()
                    .collect(Collectors.groupingBy(Order::getUser));

            List<UserSummary> userSummaries = new ArrayList<>();
            for (Map.Entry<User, List<Order>> entry : ordersByUser.entrySet()) {
                User user = entry.getKey();
                List<Order> userOrders = entry.getValue();

                // Lấy tất cả OrderItem của người này
                List<OrderItem> allUserItems = userOrders.stream()
                        .flatMap(order -> order.getItems().stream())
                        .collect(Collectors.toList());

                // Gộp các OrderItem trùng tên món ăn
                Map<Dish, Integer> mergedItemsMap = allUserItems.stream()
                        .collect(Collectors.groupingBy(
                                OrderItem::getDish,
                                Collectors.summingInt(OrderItem::getQuantity)
                        ));

                // Chuyển Map thành List<OrderItem> (fake) để dễ lặp trong view
                List<OrderItem> mergedItemsList = new ArrayList<>();
                for (Map.Entry<Dish, Integer> itemEntry : mergedItemsMap.entrySet()) {
                    OrderItem tempItem = new OrderItem();
                    tempItem.setDish(itemEntry.getKey());
                    tempItem.setQuantity(itemEntry.getValue());
                    mergedItemsList.add(tempItem);
                }

                // Tính tổng số lượng của user này
                int userTotalQuantity = allUserItems.stream()
                        .mapToInt(OrderItem::getQuantity)
                        .sum();

                // Tính tổng tiền của user này
                BigDecimal userTotalAmount = userOrders.stream()
                        .map(Order::getTotalAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                userSummaries.add(new UserSummary(user, mergedItemsList, userTotalQuantity, userTotalAmount));
            }
            model.addAttribute("userSummaries", userSummaries);
            // *** KẾT THÚC SỬA ĐỔI ***

        } else {
            model.addAttribute("groupedItems", Collections.emptyMap());
            model.addAttribute("totalQuantities", Collections.emptyMap());
            model.addAttribute("subtotals", Collections.emptyMap());
            model.addAttribute("grandTotal", BigDecimal.ZERO);

            model.addAttribute("summaryTotalAmount", BigDecimal.ZERO);
            model.addAttribute("summaryTotalQuantity", 0);

            model.addAttribute("userSummaries", Collections.emptyList());
        }

        return "home";
    }

    // ... (Các phương thức còn lại không thay đổi) ...
    @PostMapping("/upload")
    public String handleImageUpload(@RequestParam("images") List<MultipartFile> files,
                                    RedirectAttributes redirectAttributes, HttpSession session) {
        if (session.getAttribute("loggedInUser") == null) return "redirect:/login";

        try {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    imageService.saveImage(file);
                }
            }
            redirectAttributes.addFlashAttribute("successMessage", "Upload ảnh thành công!");
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Upload ảnh thất bại: " + e.getMessage());
        }
        return "redirect:/";
    }

    @PostMapping("/image/delete/{id}")
    public String deleteImage(@PathVariable Long id, RedirectAttributes redirectAttributes, HttpSession session) {
        if (session.getAttribute("loggedInUser") == null) return "redirect:/login";

        try {
            imageService.deleteImageById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Đã xóa ảnh thành công.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi xóa ảnh: " + e.getMessage());
        }
        return "redirect:/";
    }

    @PostMapping("/dishes/create")
    public String createDish(@RequestParam String name,
                             @RequestParam BigDecimal price,
                             RedirectAttributes redirectAttributes, HttpSession session) {
        if (session.getAttribute("loggedInUser") == null) return "redirect:/login";

        Dish newDish = new Dish();
        newDish.setName(name);
        newDish.setPrice(price);

        if (dishService.save(newDish) != null) {
            redirectAttributes.addFlashAttribute("successMessage", "Tạo món ăn mới thành công!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Tên món ăn đã tồn tại.");
        }
        return "redirect:/";
    }

    @PostMapping("/dishes/delete/{id}")
    public String deleteDish(@PathVariable Long id, RedirectAttributes redirectAttributes, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) return "redirect:/login";

        if (!"ADMIN".equals(loggedInUser.getRole())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bạn không có quyền thực hiện hành động này.");
            return "redirect:/";
        }

        try {
            dishService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Đã xóa món ăn thành công.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không thể xóa món ăn này. Có thể món ăn đã được đặt trong một đơn hàng nào đó.");
        }
        return "redirect:/";
    }


    @PostMapping("/cart/add")
    public String addToCart(@RequestParam Long dishId,
                            @RequestParam int quantity,
                            RedirectAttributes redirectAttributes, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) return "redirect:/login";

        if(appStateService.isOrderingLocked() && !"ADMIN".equals(loggedInUser.getRole())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Admin đã khóa chức năng đặt món.");
            return "redirect:/";
        }

        try {
            orderService.addToCart(loggedInUser.getId(), dishId, quantity);
            redirectAttributes.addFlashAttribute("successMessage", "Đã thêm món ăn vào giỏ hàng.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());
        }
        return "redirect:/";
    }

    @PostMapping("/order/complete")
    public String completeOrder(RedirectAttributes redirectAttributes, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) return "redirect:/login";

        if(appStateService.isOrderingLocked() && !"ADMIN".equals(loggedInUser.getRole())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Admin đã khóa chức năng đặt món, không thể hoàn tất.");
            return "redirect:/";
        }

        Order cart = orderService.getOrCreateCart(loggedInUser);
        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Giỏ hàng của bạn đang rỗng. Không thể hoàn tất.");
            return "redirect:/";
        }

        try {
            orderService.completeOrder(loggedInUser.getId());
            redirectAttributes.addFlashAttribute("successMessage", "Đặt cơm thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());
        }
        return "redirect:/";
    }

    @PostMapping("/order/cancel")
    public String cancelOrder(RedirectAttributes redirectAttributes, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) return "redirect:/login";

        try {
            orderService.cancelOrder(loggedInUser.getId());
            redirectAttributes.addFlashAttribute("successMessage", "Đã hủy đơn hàng.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());
        }
        return "redirect:/";
    }

    @PostMapping("/picker/random")
    public String pickRandomUsers(RedirectAttributes redirectAttributes, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) return "redirect:/login";

        AppState appState = appStateService.getAppState();

        // 1. Kiểm tra đã khóa chưa
        if (!appState.isOrderingLocked()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Chức năng này chỉ mở sau khi Admin 'Kết thúc chọn món'.");
            return "redirect:/";
        }

        // 2. Kiểm tra đã quay chưa (và quyền Admin)
        if (appState.isHasBeenSpun() && !"ADMIN".equals(loggedInUser.getRole())) {
            redirectAttributes.addFlashAttribute("infoMessage", "Đã có người quay số rồi. Chỉ Admin mới được quay lại.");
            return "redirect:/";
        }

        List<User> selectedUsers = orderService.getRandomUsersToFetchMeals();
        if (selectedUsers.isEmpty()) {
            redirectAttributes.addFlashAttribute("infoMessage", "Không có đủ người dùng nam hợp lệ (hoặc chưa ai đặt cơm) để chọn.");
        } else {
            // Ghi lại kết quả
            appStateService.recordSpin(loggedInUser, selectedUsers);

            String successMsg = "ADMIN".equals(loggedInUser.getRole()) ? "Admin đã quay lại thành công!" : "Bạn đã quay số thành công!";
            redirectAttributes.addFlashAttribute("successMessage", successMsg);
        }
        return "redirect:/";
    }
}