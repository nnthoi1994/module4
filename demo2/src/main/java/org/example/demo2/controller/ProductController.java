package org.example.demo2.controller;


import org.example.demo2.entity.Product;
import org.example.demo2.repository.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository productRepository = new ProductRepository();

    @GetMapping
    public String list(Model model) {
        model.addAttribute("products", productRepository.findAll());
        return "products";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("product", new Product());
        return "product-form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Product product) {
        // if id==0, assign new id
        if (product.getId() == 0) {
            int newId = productRepository.findAll().stream().mapToInt(Product::getId).max().orElse(0) + 1;
            product.setId(newId);
        }
        productRepository.save(product);
        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable int id, Model model) {
        Product p = productRepository.findById(id);
        model.addAttribute("product", p);
        return "product-form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        productRepository.delete(id);
        return "redirect:/products";
    }
}
