package com.example.quan_ly_san_pham.controller;



import com.example.quan_ly_san_pham.entity.User;
import com.example.quan_ly_san_pham.service.IUserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    private IUserService userService;

    /**
     * Hiển thị trang đăng nhập.
     * Nếu người dùng đã đăng nhập rồi, sẽ tự động chuyển hướng họ về trang chủ.
     */
    @GetMapping("/login")
    public String showLoginForm(HttpSession session) {
        if (session.getAttribute("loggedInUser") != null) {
            return "redirect:/";
        }
        return "login"; // Trả về file login.html
    }

    /**
     * Xử lý dữ liệu từ form đăng nhập.
     */
    @PostMapping("/login")
    public String handleLogin(@RequestParam String username,
                              @RequestParam String password,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {

        Optional<User> userOptional = userService.login(username, password);

        if (userOptional.isPresent()) {
            // Đăng nhập thành công, lưu thông tin user vào session
            session.setAttribute("loggedInUser", userOptional.get());
            return "redirect:/"; // Chuyển hướng về trang chủ
        } else {
            // Đăng nhập thất bại, gửi thông báo lỗi về lại trang login
            redirectAttributes.addFlashAttribute("errorMessage", "Tên đăng nhập hoặc mật khẩu không đúng.");
            return "redirect:/login";
        }
    }

    /**
     * Hiển thị trang đăng ký.
     */
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User()); // Tạo một đối tượng User rỗng để binding với form
        return "register"; // Trả về file register.html
    }

    /**
     * Xử lý dữ liệu từ form đăng ký.
     */
    @PostMapping("/register")
    public String handleRegister(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        // Kiểm tra xem username hoặc email đã tồn tại chưa
        if (userService.isUsernameExists(user.getUsername())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Tên đăng nhập đã tồn tại.");
            return "redirect:/register";
        }
        if (userService.isEmailExists(user.getEmail())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Email đã được sử dụng.");
            return "redirect:/register";
        }

        // Nếu mọi thứ hợp lệ, tiến hành đăng ký
        userService.register(user);
        redirectAttributes.addFlashAttribute("successMessage", "Đăng ký tài khoản thành công! Vui lòng đăng nhập.");
        return "redirect:/login";
    }

    /**
     * Xử lý đăng xuất.
     */
    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate(); // Hủy toàn bộ session
        redirectAttributes.addFlashAttribute("successMessage", "Bạn đã đăng xuất thành công.");
        return "redirect:/login";
    }
}
