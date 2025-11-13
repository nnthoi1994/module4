package com.example.demo.controller;

import com.example.demo.entity.Category;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ICategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/category")
public class CategoryController {
    private final ICategoryService categoryService;
    public CategoryController(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }
    @GetMapping
    public String showCategoryList(Model model) {
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        return "category/list";
    }
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("category", new Category());
        return "category/form";
    }
    @PostMapping("/save")
    public String saveCategory(@ModelAttribute Category category,
                               RedirectAttributes redirectAttributes) {
        if (category.getId() == null) {
            categoryService.save(category);
            redirectAttributes.addFlashAttribute("successMessage", "Thêm danh mục thành công!");
        } else {
            categoryService.save(category);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật danh mục thành công!");
        }
        return "redirect:/category";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") int id, Model model) {
        Category category = categoryService.findById(id);
        model.addAttribute("category", category);
        return "category/form";
    }
    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") int id,
                                 RedirectAttributes redirectAttributes) {
        categoryService.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Xóa danh mục thành công!");
        return "redirect:/category";
    }
}
