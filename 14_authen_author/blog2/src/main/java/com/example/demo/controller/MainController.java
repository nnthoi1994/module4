package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/login")
    public String loginPage() {
        return "loginPage"; // Tên file html login
    }

    @GetMapping("/403")
    public String accessDenied() {
        return "403Page"; // Tên file html 403
    }
}