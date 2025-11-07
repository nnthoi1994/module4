package com.example.ordermeal.controller;

import com.example.ordermeal.entity.User;
import com.example.ordermeal.service.IUserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final IUserService userService;

    @GetMapping("/login")
    public String showLoginForm(HttpSession session) {
        if (session.getAttribute("loggedInUser") != null) {
            return "redirect:/";
        }
        return "login";
    }

    @PostMapping("/login")
    public String handleLogin(@RequestParam String username,
                              @RequestParam String password,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        Optional<User> userOptional = userService.login(username, password);

        if (userOptional.isPresent()) {
            session.setAttribute("loggedInUser", userOptional.get());
            return "redirect:/";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Tên đăng nhập hoặc mật khẩu không đúng.");
            return "redirect:/login";
        }
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String handleRegister(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        if (userService.isUsernameExists(user.getUsername())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Tên đăng nhập đã tồn tại.");
            return "redirect:/register";
        }
        if (userService.isEmailExists(user.getEmail())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Email đã được sử dụng.");
            return "redirect:/register";
        }

        userService.register(user);
        redirectAttributes.addFlashAttribute("successMessage", "Đăng ký tài khoản thành công! Vui lòng đăng nhập.");
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("successMessage", "Bạn đã đăng xuất thành công.");
        return "redirect:/login";
    }
}