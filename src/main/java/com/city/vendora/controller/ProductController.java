package com.city.vendora.controller;

import com.city.vendora.entity.Category;
import com.city.vendora.entity.Product;
import com.city.vendora.repository.CategoryRepository;
import com.city.vendora.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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
        Product product = new Product();
        product.setCategory(new Category());
        model.addAttribute("product", product);
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
    public String save(@RequestParam String productId,
                       @RequestParam String productName,
                       @RequestParam Integer categoryId,
                       @RequestParam(defaultValue = "1") Integer status) {
        Product product = new Product();
        product.setProductId(productId);
        product.setProductName(productName);
        product.setStatus(status);
        product.setCategory(categoryRepository.findById(categoryId).orElse(null));

        Product existing = productService.getProductById(productId);
        if (existing != null) {
            product.setCreateTime(existing.getCreateTime());
            productService.modifyProduct(product);
        } else {
            product.setCreateTime(java.time.LocalDateTime.now());
            productService.addProduct(product);
        }
        return "redirect:/product/list";
    }

    @GetMapping("/get/{productId}")
    @ResponseBody
    public Map<String, Object> getProduct(@PathVariable String productId) {
        Product p = productService.getProductById(productId);
        if (p == null) {
            Map<String, Object> err = new HashMap<>();
            err.put("error", "商品不存在");
            return err;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("productId", p.getProductId());
        map.put("productName", p.getProductName());
        map.put("categoryId", p.getCategory().getId());
        map.put("status", p.getStatus());
        return map;
    }

    @GetMapping("/disable/{productId}")
    public String disable(@PathVariable String productId) {
        productService.disableProduct(productId);
        return "redirect:/product/list";
    }

    @GetMapping("/enable/{productId}")
    public String enable(@PathVariable String productId) {
        productService.enableProduct(productId);
        return "redirect:/product/list";
    }
}
