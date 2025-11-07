package com.example.gio_hang.controller;

import com.example.gio_hang.entity.Cart;
import com.example.gio_hang.entity.Product;
import com.example.gio_hang.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@SessionAttributes("cart")
public class ProductController {
    @Autowired
    private IProductService productService;

    @ModelAttribute("cart")
    public Cart setupCart() {
        return new Cart();
    }

    @GetMapping("/shop")
    public ModelAndView showShop() {
        ModelAndView modelAndView = new ModelAndView("/shop");
        modelAndView.addObject("products", productService.findAll());
        return modelAndView;
    }

    // --- PHƯƠNG THỨC MỚI ---
    /**
     * Hiển thị trang chi tiết sản phẩm
     */
    @GetMapping("/shop/view/{id}")
    public ModelAndView showProductDetail(@PathVariable Long id) {
        Optional<Product> productOptional = productService.findById(id);
        if (!productOptional.isPresent()) {
            // Nếu không tìm thấy, quay về trang lỗi
            return new ModelAndView("/error_404");
        }

        ModelAndView modelAndView = new ModelAndView("/detail");
        modelAndView.addObject("product", productOptional.get());
        return modelAndView;
    }


    @GetMapping("/add/{id}")
    public String addToCart(@PathVariable Long id,
                            @ModelAttribute Cart cart,
                            @RequestParam("action") String action) {
        Optional<Product> productOptional = productService.findById(id);
        if (!productOptional.isPresent()) {
            return "/error_404";
        }
        Product product = productOptional.get();

        if (action.equals("show")) {
            // Thêm vào giỏ hàng và chuyển đến trang giỏ hàng
            cart.addProduct(product);
            return "redirect:/shopping-cart";
        }

        if (action.equals("detail")) {
            // Thêm vào giỏ hàng và ở lại trang chi tiết
            cart.addProduct(product);
            return "redirect:/shop/view/" + id; // Quay lại trang chi tiết
        }

        // Thêm vào giỏ hàng và ở lại trang shop
        cart.addProduct(product);
        return "redirect:/shop";
    }
}
