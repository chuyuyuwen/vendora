package com.city.vendora.controller;

import com.city.vendora.entity.Product;
import com.city.vendora.repository.CategoryRepository;
import com.city.vendora.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;
    private final CategoryRepository categoryRepository;

    public ProductController(ProductService productService, CategoryRepository categoryRepository) {
        this.productService = productService;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/list")
    public String list(@RequestParam(required = false) String productId,
                       @RequestParam(required = false) String productName,
                       @RequestParam(required = false) Integer categoryId,
                       Model model) {
        model.addAttribute("products", productService.searchProducts(productId, productName, categoryId));
        model.addAttribute("productId", productId);
        model.addAttribute("productName", productName);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("categories", categoryRepository.findAll());
        return "product/list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryRepository.findAll());
        return "product/form";
    }

    @GetMapping("/edit/{productId}")
    public String editForm(@PathVariable String productId, Model model) {
        Product product = productService.getProductById(productId);
        if (product == null) {
            return "redirect:/product/list";
        }
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryRepository.findAll());
        return "product/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Product product) {
        if (productService.getProductById(product.getProductId()) != null) {
            productService.modifyProduct(product);
        } else {
            productService.addProduct(product);
        }
        return "redirect:/product/list";
    }

    @GetMapping("/disable/{productId}")
    public String disable(@PathVariable String productId) {
        productService.disableProduct(productId);
        return "redirect:/product/list";
    }
}
