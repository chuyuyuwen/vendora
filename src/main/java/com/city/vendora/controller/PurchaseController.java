package com.city.vendora.controller;

import com.city.vendora.entity.Product;
import com.city.vendora.entity.Purchase;
import com.city.vendora.service.ProductService;
import com.city.vendora.service.PurchaseService;
import com.city.vendora.util.PageBean;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

@Controller
@RequestMapping("/purchase")
public class PurchaseController {

    private final PurchaseService purchaseService;
    private final ProductService productService;

    public PurchaseController(PurchaseService purchaseService, ProductService productService) {
        this.purchaseService = purchaseService;
        this.productService = productService;
    }

    @GetMapping("/list")
    public String list(@RequestParam(required = false) String productId,
                       @RequestParam(required = false) @DateTimeFormat(iso = DATE) LocalDate startDate,
                       @RequestParam(required = false) @DateTimeFormat(iso = DATE) LocalDate endDate,
                       @RequestParam(defaultValue = "1") int pageNo,
                       @RequestParam(defaultValue = "10") int pageSize,
                       Model model) {
        PageBean<Purchase> page = purchaseService.queryPurchases(productId, startDate, endDate, pageNo, pageSize);
        model.addAttribute("page", page);
        model.addAttribute("productId", productId);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("products", productService.listAllProducts());
        return "purchase/list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        Purchase purchase = new Purchase();
        purchase.setProduct(new Product());
        model.addAttribute("purchase", purchase);
        model.addAttribute("products", productService.listAllProducts());
        return "purchase/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Purchase purchase, RedirectAttributes ra) {
        boolean ok = purchaseService.addPurchase(purchase);
        if (ok) {
            ra.addFlashAttribute("msg", "买入录入成功");
        } else {
            ra.addFlashAttribute("msg", "买入录入失败");
        }
        return "redirect:/purchase/list";
    }

    @GetMapping("/print")
    public String print(@RequestParam(required = false) @DateTimeFormat(iso = DATE) LocalDate startDate,
                        @RequestParam(required = false) @DateTimeFormat(iso = DATE) LocalDate endDate,
                        Model model) {
        List<Map<String, Object>> summary = purchaseService.getPurchaseSummary(startDate, endDate);
        model.addAttribute("summary", summary);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        return "purchase/print";
    }
}
