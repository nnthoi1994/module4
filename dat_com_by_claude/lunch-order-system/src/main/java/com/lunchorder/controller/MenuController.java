package com.lunchorder.controller;

import com.lunchorder.model.User;
import com.lunchorder.service.MenuImageService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuImageService menuImageService;

    @PostMapping("/upload")
    public String uploadImages(@RequestParam("images") List<MultipartFile> files,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        try {
            User currentUser = (User) session.getAttribute("currentUser");
            menuImageService.uploadImages(files, currentUser);
            redirectAttributes.addFlashAttribute("success", "Đã tải lên " + files.size() + " ảnh thực đơn");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi tải ảnh: " + e.getMessage());
        }
        return "redirect:/";
    }
}