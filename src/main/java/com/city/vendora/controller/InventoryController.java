package com.city.vendora.controller;

import com.city.vendora.entity.Inventory;
import com.city.vendora.repository.CategoryRepository;
import com.city.vendora.service.InventoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;
    private final CategoryRepository categoryRepository;

    public InventoryController(InventoryService inventoryService, CategoryRepository categoryRepository) {
        this.inventoryService = inventoryService;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/list")
    public String list(@RequestParam(required = false) String productId,
                       @RequestParam(required = false) Integer categoryId,
                       Model model) {
        List<Inventory> inventories = inventoryService.getInventoryList(productId, categoryId);
        model.addAttribute("inventories", inventories);
        model.addAttribute("productId", productId);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("categories", categoryRepository.findAll());
        return "inventory/list";
    }

    @GetMapping("/alert")
    public String alert(@RequestParam(defaultValue = "10") int threshold, Model model) {
        List<Inventory> lowStock = inventoryService.getLowStockProducts(threshold);
        model.addAttribute("lowStock", lowStock);
        model.addAttribute("threshold", threshold);
        return "inventory/alert";
    }
}
