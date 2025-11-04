package com.example.ordermeal.controller;

import com.example.ordermeal.entity.*;
import com.example.ordermeal.service.*;
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
    private final IUserService userService;
    private final ImageService imageService;
    private final DishService dishService;
    private final OrderService orderService;
    private final AppStateService appStateService;

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
        model.addAttribute("isOrderingLocked", appStateService.isOrderingLocked());

        // Get cart
        Order cart = orderService.getOrCreateCart(loggedInUser);
        model.addAttribute("cart", cart);

        // Get today's completed orders
        List<Order> todaysCompletedOrders = orderService.getTodaysCompletedOrders();
        model.addAttribute("todaysCompletedOrders", todaysCompletedOrders);

        // Process summary data only if there are completed orders
        if (todaysCompletedOrders != null && !todaysCompletedOrders.isEmpty()) {
            List<OrderItem> allItems = todaysCompletedOrders.stream()
                    .filter(order -> order.getItems() != null)
                    .flatMap(order -> order.getItems().stream())
                    .collect(Collectors.toList());

            if (!allItems.isEmpty()) {
                Map<String, List<OrderItem>> groupedItems = allItems.stream()
                        .collect(Collectors.groupingBy(item -> item.getDish().getName()));

                BigDecimal grandTotal = allItems.stream()
                        .map(item -> {
                            BigDecimal price = item.getPricePerItem() != null ? item.getPricePerItem() : BigDecimal.ZERO;
                            return price.multiply(new BigDecimal(item.getQuantity()));
                        })
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                model.addAttribute("groupedItems", groupedItems);
                model.addAttribute("grandTotal", grandTotal);
            } else {
                model.addAttribute("groupedItems", Collections.emptyMap());
                model.addAttribute("grandTotal", BigDecimal.ZERO);
            }
        } else {
            model.addAttribute("groupedItems", Collections.emptyMap());
            model.addAttribute("grandTotal", BigDecimal.ZERO);
        }

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
            redirectAttributes.addFlashAttribute("errorMessage", "Upload ảnh thất bại: " + e.getMessage());
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
}