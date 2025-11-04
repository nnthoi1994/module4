package com.example.quan_ly_san_pham.controller;

import com.example.quan_ly_san_pham.entity.*;
import com.example.quan_ly_san_pham.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired private IUserService userService;
    @Autowired private ImageService imageService;
    @Autowired private DishService dishService;
    @Autowired private OrderService orderService;
    @Autowired private AppStateService appStateService;

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

        model.addAttribute("todaysImages", imageService.getTodaysImages());
        model.addAttribute("dishes", dishService.findAll());

        List<Order> todaysCompletedOrders = orderService.getTodaysCompletedOrders();
        model.addAttribute("todaysCompletedOrders", todaysCompletedOrders);
        model.addAttribute("isOrderingLocked", appStateService.isOrderingLocked());

        Order cart = orderService.getOrCreateCart(loggedInUser);
        model.addAttribute("cart", cart);

        // === FIX: Xử lý logic tổng hợp ở đây thay vì trong template ===

        // Lấy tất cả items từ các orders đã hoàn tất
        List<OrderItem> allItems = todaysCompletedOrders.stream()
                .flatMap(order -> order.getItems().stream())
                .collect(Collectors.toList());

        // Nhóm các items theo tên món ăn
        Map<String, List<OrderItem>> groupedItems = allItems.stream()
                .collect(Collectors.groupingBy(item -> item.getDish().getName()));

        // Tính tổng tiền
        BigDecimal grandTotal = allItems.stream()
                .map(item -> item.getPricePerItem().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("groupedItems", groupedItems);
        model.addAttribute("grandTotal", grandTotal);

        return "home";
    }

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
            redirectAttributes.addFlashAttribute("errorMessage", "Upload ảnh thất bại.");
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

        orderService.addToCart(loggedInUser.getId(), dishId, quantity);
        redirectAttributes.addFlashAttribute("successMessage", "Đã thêm món ăn vào giỏ hàng.");
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

        orderService.completeOrder(loggedInUser.getId());
        redirectAttributes.addFlashAttribute("successMessage", "Đặt cơm thành công!");
        return "redirect:/";
    }

    @PostMapping("/order/cancel")
    public String cancelOrder(RedirectAttributes redirectAttributes, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) return "redirect:/login";

        orderService.cancelOrder(loggedInUser.getId());
        redirectAttributes.addFlashAttribute("successMessage", "Đã hủy đơn hàng.");
        return "redirect:/";
    }
}