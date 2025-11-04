package com.bai_tap.controller;

import com.bai_tap.entity.Blog;
import com.bai_tap.service.IBlogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/blogs")
public class BlogController {
    private final IBlogService blogService;

    public BlogController(IBlogService blogService) {
        this.blogService = blogService;
    }
    @GetMapping
    public String showBlogList(@RequestParam(value = "keyword", required = false) String keyword, Model model) {

        List<Blog> blogs;
        if (keyword != null && !keyword.trim().isEmpty()) {
            blogs = blogService.searchByName(keyword);
        } else {
            blogs = blogService.findAll();
        }
        model.addAttribute("blogs", blogs);
        model.addAttribute("keyword", keyword);
        return "blog/list";
    }


    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("blog", new Blog());
        return "blog/form";
    }


    @PostMapping("/save")
    public String saveBlog(@ModelAttribute Blog blog,
                           RedirectAttributes redirectAttributes) {
        if (blog.getId() == 0) {
            blogService.save(blog);
            redirectAttributes.addFlashAttribute("successMessage", " Thêm blog thành công");
        } else {
            blogService.save(blog);
            redirectAttributes.addFlashAttribute("successMessage", " Cập nhật blog thành công");
        }
        return "redirect:/blogs";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") int id, Model model) {
        Blog blog = blogService.findById(id);
        model.addAttribute("blog", blog);
        return "blog/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") int id,
                                RedirectAttributes redirectAttributes) {
        blogService.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", " Xóa blog thành công");
        return "redirect:/blogs";
    }

    @GetMapping("/{id}")
    public String viewDetail(@PathVariable("id") int id, Model model) {
        Blog blog = blogService.findById(id);
        model.addAttribute("blog", blog);
        return "blog/detail";
    }
}
