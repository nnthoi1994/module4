package com.example.demo.controller;


import com.example.demo.entity.Blog;
import com.example.demo.service.IBlogService;
import com.example.demo.service.ICategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/blogs")
public class BlogController {
    private final IBlogService blogService;
    private final ICategoryService categoryService;


    public BlogController(IBlogService blogService, ICategoryService categoryService) {
        this.blogService = blogService;
        this.categoryService = categoryService;
    }
    @GetMapping
    public String showBlogList(@RequestParam(value = "keyword", required = false) String keyword,
                               @RequestParam(value = "categoryId", required = false) Integer categoryId,
                               @RequestParam(defaultValue = "0") int page,
                               Model model) {

        int pageSize = 5;
        Pageable pageable = PageRequest.of(page, pageSize);

        Page<Blog> blogPage = blogService.searchByTitleAndCategory(keyword, categoryId, pageable);

        model.addAttribute("blogs", blogPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", blogPage.getTotalPages());
        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("categories", categoryService.findAll());

        return "blog/list";
    }




    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("blog", new Blog());
        model.addAttribute("categories", categoryService.findAll());
        return "blog/form";
    }

    @PostMapping("/save")
    public String saveBlog(@ModelAttribute Blog blog,
                           RedirectAttributes redirectAttributes, Principal principal) {

        if (principal != null) {
            blog.setAuthor(principal.getName());
        }

        if (blog.getId() == null) {
            blogService.save(blog);
            redirectAttributes.addFlashAttribute("successMessage", "Thêm blog thành công!");
        } else {
            blogService.save(blog);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật blog thành công!");
        }
        return "redirect:/blogs";
    }


    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") int id, Model model) {
        Blog blog = blogService.findById(id);
        model.addAttribute("blog", blog);
        model.addAttribute("categories", categoryService.findAll());
        return "blog/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteBlog(@PathVariable("id") int id,
                             RedirectAttributes redirectAttributes) {
        blogService.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Xóa blog thành công!");
        return "redirect:/blogs";
    }
    @GetMapping("/{id}")
    public String viewDetail(@PathVariable("id") int id, Model model) {
        Blog blog = blogService.findById(id);
        model.addAttribute("blog", blog);
        return "blog/detail";
    }


}
