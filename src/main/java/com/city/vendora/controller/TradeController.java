package com.city.vendora.controller;

import com.city.vendora.service.ProductService;
import com.city.vendora.service.PurchaseService;
import com.city.vendora.service.SaleService;
import com.city.vendora.util.PageBean;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

@Controller
@RequestMapping("/trade")
public class TradeController {

    private final PurchaseService purchaseService;
    private final SaleService saleService;
    private final ProductService productService;

    public TradeController(PurchaseService purchaseService,
                           SaleService saleService,
                           ProductService productService) {
        this.purchaseService = purchaseService;
        this.saleService = saleService;
        this.productService = productService;
    }

    @GetMapping
    public String trade(@RequestParam(defaultValue = "purchase") String tab,
                        @RequestParam(required = false) String productId,
                        @RequestParam(required = false) @DateTimeFormat(iso = DATE) LocalDate startDate,
                        @RequestParam(required = false) @DateTimeFormat(iso = DATE) LocalDate endDate,
                        @RequestParam(defaultValue = "1") int pageNo,
                        @RequestParam(defaultValue = "10") int pageSize,
                        Model model) {
        if (productId != null && productId.isEmpty()) productId = null;

        model.addAttribute("tab", tab);
        model.addAttribute("productId", productId);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("products", productService.listAllProducts());

        if ("sale".equals(tab)) {
            model.addAttribute("salePage", saleService.querySales(productId, startDate, endDate, pageNo, pageSize));
        } else {
            model.addAttribute("purchasePage", purchaseService.queryPurchases(productId, startDate, endDate, pageNo, pageSize));
        }

        return "trade/list";
    }
}
