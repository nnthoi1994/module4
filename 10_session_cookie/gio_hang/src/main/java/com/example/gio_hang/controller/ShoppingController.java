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
public class ShoppingController {


    @Autowired
    private IProductService productService;

    @ModelAttribute("cart")
    public Cart setupCart() {
        return new Cart();
    }

    @GetMapping("/shopping-cart")
    public ModelAndView showCart(@SessionAttribute(value = "cart", required = false) Cart cart) {
        ModelAndView modelAndView = new ModelAndView("/cart");

        if (cart == null) {
            cart = new Cart();
        }
        modelAndView.addObject("cart", cart);
        return modelAndView;
    }


    @PostMapping("/shopping-cart/update")
    public String updateCart(@RequestParam("id") Long id,
                             @RequestParam("quantity") Integer quantity,
                             @SessionAttribute("cart") Cart cart) {

        Optional<Product> productOptional = productService.findById(id);
        if (productOptional.isPresent() && quantity != null) {
            cart.updateQuantity(productOptional.get(), quantity);
        }
        return "redirect:/shopping-cart";
    }


    @GetMapping("/shopping-cart/remove/{id}")
    public String removeCartItem(@PathVariable Long id,
                                 @SessionAttribute("cart") Cart cart) {

        Optional<Product> productOptional = productService.findById(id);
        productOptional.ifPresent(cart::removeProduct); // Cú pháp Java 8

        return "redirect:/shopping-cart";
    }


    @GetMapping("/checkout")
    public ModelAndView showCheckout(@SessionAttribute("cart") Cart cart) {
        ModelAndView modelAndView = new ModelAndView("/order");
        modelAndView.addObject("cart", cart);
        return modelAndView;
    }


    @PostMapping("/payment")
    public String processPayment(@SessionAttribute("cart") Cart cart) {

        cart.clearCart();


        return "redirect:/payment-success";
    }


    @GetMapping("/payment-success")
    public String showPaymentSuccess() {
        return "/payment-success";
    }
}
