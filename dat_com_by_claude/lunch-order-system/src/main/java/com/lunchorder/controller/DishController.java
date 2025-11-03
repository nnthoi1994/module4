package com.lunchorder.controller;

import com.lunchorder.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @PostMapping("/create")
    public String createDish(@RequestParam String name,
                             @RequestParam Double price,
                             RedirectAttributes redirectAttributes) {
        try {
            dishService.createDish(name, price);
            redirectAttributes.addFlashAttribute("success", "Đã tạo món ăn mới: " + name);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/";
    }
}