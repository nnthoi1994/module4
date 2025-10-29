package org.example.demo2.controller;


import org.example.demo2.entity.EmailSettings;
import org.example.demo2.service.IEmailSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class SettingsController {

    @Autowired
    private IEmailSettingsService emailSettingsService;

    @GetMapping("/settings")
    public String showSettingsForm(Model model) {

        model.addAttribute("settings", emailSettingsService.getSettings());
        model.addAttribute("languages", emailSettingsService.getAllLanguages());
        model.addAttribute("pageSizes", emailSettingsService.getAllPageSizes());


        return "settings";
    }

    @PostMapping("/update-settings")
    public String updateSettings(@ModelAttribute("settings") EmailSettings updatedSettings, RedirectAttributes redirectAttributes) {

        emailSettingsService.save(updatedSettings);


        redirectAttributes.addFlashAttribute("message", "Settings updated successfully!");


        return "redirect:/result";
    }

    @GetMapping("/result")
    public String showResult(Model model) {

        model.addAttribute("updatedSettings", emailSettingsService.getSettings());
        return "result";
    }
}
