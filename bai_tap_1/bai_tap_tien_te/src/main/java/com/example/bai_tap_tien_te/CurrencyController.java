package com.example.bai_tap_1;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CurrencyController {
    private static final double rate= 26000;
    @GetMapping("/home")
    public String showForm() {
        return "home";
    }
    @PostMapping("/convert")
    public String convert(@RequestParam("usd") double usd, Model model) {
        double vnd = usd * rate;
        model.addAttribute("usd", usd);
        model.addAttribute("vnd", vnd);
        model.addAttribute("rate", rate);
        return "result";
    }

}