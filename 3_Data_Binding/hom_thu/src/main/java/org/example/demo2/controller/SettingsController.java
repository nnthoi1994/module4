package org.example.demo2.controller;


import org.example.demo2.entity.EmailSettings;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;

@Controller
public class SettingsController {

    // Giả lập database: lưu trữ cấu hình hiện tại
    private static EmailSettings currentSettings = new EmailSettings("English", 25, true, "Thor\nKing, Asgard");

    @GetMapping("/settings")
    public ModelAndView showSettingsForm() {
        ModelAndView modelAndView = new ModelAndView("settings");

        // Dữ liệu cho các dropdown list
        List<String> languages = Arrays.asList("English", "Vietnamese", "Japanese", "Chinese");
        List<Integer> pageSizes = Arrays.asList(5, 10, 15, 25, 50, 100);

        // Đưa dữ liệu vào model để view có thể truy cập
        modelAndView.addObject("settings", currentSettings);
        modelAndView.addObject("languages", languages);
        modelAndView.addObject("pageSizes", pageSizes);

        return modelAndView;
    }

    @PostMapping("/update-settings")
    public ModelAndView updateSettings(@ModelAttribute("settings") EmailSettings updatedSettings) {
        // Cập nhật cấu hình hiện tại từ dữ liệu form gửi lên
        currentSettings = updatedSettings;

        ModelAndView modelAndView = new ModelAndView("result");
        modelAndView.addObject("message", "Settings updated successfully!");
        modelAndView.addObject("updatedSettings", currentSettings);

        return modelAndView;
    }
}
