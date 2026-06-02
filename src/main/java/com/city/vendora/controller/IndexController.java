package com.city.vendora.controller;

import com.city.vendora.service.InventoryService;
import com.city.vendora.service.ProductService;
import com.city.vendora.service.PurchaseService;
import com.city.vendora.service.ReportService;
import com.city.vendora.service.SaleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;

@Controller
public class IndexController {

    private final ProductService productService;
    private final SaleService saleService;
    private final PurchaseService purchaseService;
    private final InventoryService inventoryService;
    private final ReportService reportService;

    public IndexController(ProductService productService, SaleService saleService,
                           PurchaseService purchaseService, InventoryService inventoryService,
                           ReportService reportService) {
        this.productService = productService;
        this.saleService = saleService;
        this.purchaseService = purchaseService;
        this.inventoryService = inventoryService;
        this.reportService = reportService;
    }

    @GetMapping("/")
    public String index(Model model) {
        // 统计卡片数据
        long totalProducts = productService.countAllProducts();
        long disabledProducts = productService.countDisabledProducts();
        long lowStockCount = inventoryService.getLowStockProducts(10).size();

        model.addAttribute("totalProducts", totalProducts);
        model.addAttribute("activeProducts", totalProducts - disabledProducts);
        model.addAttribute("disabledProducts", disabledProducts);
        model.addAttribute("lowStockCount", lowStockCount);

        model.addAttribute("todaySalesAmount", saleService.getTodaySalesAmount());
        model.addAttribute("todaySalesCount", saleService.getTodaySalesCount());
        model.addAttribute("todayPurchasesAmount", purchaseService.getTodayPurchasesAmount());
        model.addAttribute("todayPurchasesCount", purchaseService.getTodayPurchasesCount());

        // 饼图数据：本月各类别销售额占比
        LocalDate monthStart = LocalDate.now().withDayOfMonth(1);
        model.addAttribute("salesByCategory", reportService.getSalesReport(monthStart, LocalDate.now(), null));

        // 折线图数据：近 30 天每日销售额趋势
        model.addAttribute("dailySales", saleService.getDailySalesTrend(LocalDate.now().minusDays(29), LocalDate.now()));

        return "index";
    }
}
