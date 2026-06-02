package com.city.vendora.controller;

import com.city.vendora.entity.Product;
import com.city.vendora.entity.Sale;
import com.city.vendora.service.ProductService;
import com.city.vendora.service.SaleService;
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
@RequestMapping("/sale")
public class SaleController {

    private final SaleService saleService;
    private final ProductService productService;

    public SaleController(SaleService saleService, ProductService productService) {
        this.saleService = saleService;
        this.productService = productService;
    }

    @GetMapping("/list")
    public String list(@RequestParam(required = false) String productId,
                       @RequestParam(required = false) @DateTimeFormat(iso = DATE) LocalDate startDate,
                       @RequestParam(required = false) @DateTimeFormat(iso = DATE) LocalDate endDate,
                       @RequestParam(defaultValue = "1") int pageNo,
                       @RequestParam(defaultValue = "10") int pageSize,
                       Model model) {
        // 空字符串转 null，避免分页链接传入空参数导致查询不生效
        if (productId != null && productId.isEmpty()) productId = null;

        PageBean<Sale> page = saleService.querySales(productId, startDate, endDate, pageNo, pageSize);
        model.addAttribute("page", page);
        model.addAttribute("productId", productId);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("products", productService.listAllProducts());
        return "sale/list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        Sale sale = new Sale();
        sale.setProduct(new Product());
        model.addAttribute("sale", sale);
        model.addAttribute("products", productService.listAllProducts());
        return "sale/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Sale sale, RedirectAttributes ra) {
        boolean ok = saleService.addSale(sale);
        if (ok) {
            ra.addFlashAttribute("msg", "卖出录入成功");
        } else {
            ra.addFlashAttribute("error", "库存不足，无法出售");
        }
        return "redirect:/sale/list";
    }

    @GetMapping("/print")
    public String print(@RequestParam(required = false) @DateTimeFormat(iso = DATE) LocalDate startDate,
                        @RequestParam(required = false) @DateTimeFormat(iso = DATE) LocalDate endDate,
                        Model model) {
        List<Map<String, Object>> summary = saleService.getSaleSummary(startDate, endDate);
        model.addAttribute("summary", summary);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        return "sale/print";
    }
}
