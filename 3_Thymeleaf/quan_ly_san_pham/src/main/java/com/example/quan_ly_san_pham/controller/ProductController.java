package com.example.quan_ly_san_pham.controller;

import com.example.quan_ly_san_pham.entity.Product;
import com.example.quan_ly_san_pham.service.IProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final IProductService productService;

    public ProductController(IProductService productService) {
        this.productService = productService;
    }
    @GetMapping
    public String showProductList(@RequestParam(value = "keyword", required = false) String keyword, Model model) {

        List<Product> products;
        if (keyword != null && !keyword.trim().isEmpty()) {
            products = productService.searchByName(keyword);
        } else {
            products = productService.findAll();
        }
        model.addAttribute("products", products);
        model.addAttribute("keyword", keyword);
        return "product/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new Product());
        return "product/form";
    }

    @PostMapping("/save")
    public String saveProduct(@ModelAttribute Product product,
                              RedirectAttributes redirectAttributes) {
        if (product.getId() == 0) {
            productService.save(product);
            redirectAttributes.addFlashAttribute("successMessage", " Thêm sản phẩm thành công");
        } else {
            productService.save(product);
            redirectAttributes.addFlashAttribute("successMessage", " Cập nhật sản phẩm thành công");
        }
        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") int id, Model model) {
        Product product = productService.findById(id);
        model.addAttribute("product", product);
        return "product/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") int id,
                                RedirectAttributes redirectAttributes) {
        productService.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", " Xóa sản phẩm thành công");
        return "redirect:/products";
    }

    @GetMapping("/{id}")
    public String viewDetail(@PathVariable("id") int id, Model model) {
        Product product = productService.findById(id);
        model.addAttribute("product", product);
        return "detail";
    }
}
