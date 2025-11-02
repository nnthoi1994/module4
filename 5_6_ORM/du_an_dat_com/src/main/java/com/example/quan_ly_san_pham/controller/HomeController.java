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

@Controller
@RequestMapping("/") // Tất cả các request trong controller này đều bắt đầu bằng "/"
public class HomeController {

    @Autowired private IUserService userService;
    @Autowired private ImageService imageService;
    @Autowired private DishService dishService;
    @Autowired private OrderService orderService;
    @Autowired private AppStateService appStateService;

    /**
     * Phương thức này sẽ tự động được gọi trước mỗi request trong controller.
     * Nó kiểm tra session và thêm thông tin user vào Model nếu họ đã đăng nhập.
     * Giúp chúng ta không cần lặp lại code lấy user từ session trong mỗi hàm.
     */
    @ModelAttribute
    public void addUserToModel(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            // Tải lại thông tin user từ DB để đảm bảo dữ liệu luôn mới nhất
            userService.findById(loggedInUser.getId()).ifPresent(user -> model.addAttribute("loggedInUser", user));
        }
    }

    /**
     * Hiển thị trang chủ. Đây là trang chính của ứng dụng.
     */
    @GetMapping
    public String homePage(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            // Nếu chưa đăng nhập, chuyển hướng về trang login
            return "redirect:/login";
        }

        // Tải tất cả dữ liệu cần thiết cho trang chủ
        model.addAttribute("todaysImages", imageService.getTodaysImages());
        model.addAttribute("dishes", dishService.findAll());
        model.addAttribute("todaysCompletedOrders", orderService.getTodaysCompletedOrders());
        model.addAttribute("isOrderingLocked", appStateService.isOrderingLocked());

        // Lấy giỏ hàng hiện tại của người dùng để hiển thị
        Order cart = orderService.getOrCreateCart(loggedInUser);
        model.addAttribute("cart", cart);

        return "home"; // Trả về file home.html
    }

    /**
     * Xử lý việc upload một hoặc nhiều file ảnh.
     */
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

    /**
     * Xử lý việc tạo một món ăn mới.
     */
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

    /**
     * Xử lý việc thêm món ăn vào giỏ hàng.
     */
    @PostMapping("/cart/add")
    public String addToCart(@RequestParam Long dishId,
                            @RequestParam int quantity,
                            RedirectAttributes redirectAttributes, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) return "redirect:/login";

        // Kiểm tra xem admin có khóa đặt món không
        if(appStateService.isOrderingLocked() && !"ADMIN".equals(loggedInUser.getRole())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Admin đã khóa chức năng đặt món.");
            return "redirect:/";
        }

        orderService.addToCart(loggedInUser.getId(), dishId, quantity);
        redirectAttributes.addFlashAttribute("successMessage", "Đã thêm món ăn vào giỏ hàng.");
        return "redirect:/";
    }

    /**
     * Xử lý khi người dùng bấm nút "Hoàn tất" đơn hàng.
     */
    @PostMapping("/order/complete")
    public String completeOrder(RedirectAttributes redirectAttributes, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) return "redirect:/login";

        // Kiểm tra lại lần nữa trước khi hoàn tất
        if(appStateService.isOrderingLocked() && !"ADMIN".equals(loggedInUser.getRole())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Admin đã khóa chức năng đặt món, không thể hoàn tất.");
            return "redirect:/";
        }

        orderService.completeOrder(loggedInUser.getId());
        redirectAttributes.addFlashAttribute("successMessage", "Đặt cơm thành công!");
        return "redirect:/";
    }

    /**
     * Xử lý khi người dùng bấm nút "Hủy" để xóa giỏ hàng hiện tại.
     */
    @PostMapping("/order/cancel")
    public String cancelOrder(RedirectAttributes redirectAttributes, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) return "redirect:/login";

        orderService.cancelOrder(loggedInUser.getId());
        redirectAttributes.addFlashAttribute("successMessage", "Đã hủy đơn hàng.");
        return "redirect:/";
    }
}
