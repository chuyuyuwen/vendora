package com.city.vendora.controller;

import com.city.vendora.repository.CategoryRepository;
import com.city.vendora.service.InventoryService;
import com.city.vendora.service.ProductService;
import com.city.vendora.service.PurchaseService;
import com.city.vendora.service.ReportService;
import com.city.vendora.service.SaleService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

@Controller
public class IndexController {

    private final ProductService productService;
    private final SaleService saleService;
    private final PurchaseService purchaseService;
    private final InventoryService inventoryService;
    private final ReportService reportService;
    private final CategoryRepository categoryRepository;

    public IndexController(ProductService productService, SaleService saleService,
                           PurchaseService purchaseService, InventoryService inventoryService,
                           ReportService reportService, CategoryRepository categoryRepository) {
        this.productService = productService;
        this.saleService = saleService;
        this.purchaseService = purchaseService;
        this.inventoryService = inventoryService;
        this.reportService = reportService;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/")
    public String index(@RequestParam(required = false) @DateTimeFormat(iso = DATE) LocalDate startDate,
                        @RequestParam(required = false) @DateTimeFormat(iso = DATE) LocalDate endDate,
                        @RequestParam(required = false) Integer categoryId,
                        Model model) {
        long totalProducts = productService.countAllProducts();
        long disabledProducts = productService.countDisabledProducts();
        long lowStockCount = inventoryService.getLowStockProducts(10).size();

        model.addAttribute("totalProducts", totalProducts);
        model.addAttribute("activeProducts", totalProducts - disabledProducts);
        model.addAttribute("disabledProducts", disabledProducts);
        model.addAttribute("lowStockCount", lowStockCount);

        BigDecimal todaySalesAmount = saleService.getTodaySalesAmount();
        model.addAttribute("todaySalesAmount", todaySalesAmount != null ? todaySalesAmount : BigDecimal.ZERO);
        model.addAttribute("todaySalesCount", saleService.getTodaySalesCount());

        BigDecimal todayPurchasesAmount = purchaseService.getTodayPurchasesAmount();
        model.addAttribute("todayPurchasesAmount", todayPurchasesAmount != null ? todayPurchasesAmount : BigDecimal.ZERO);
        model.addAttribute("todayPurchasesCount", purchaseService.getTodayPurchasesCount());

        // 饼图 JSON
        try {
            LocalDate monthStart = LocalDate.now().withDayOfMonth(1);
            List<Map<String, Object>> pieData = reportService.getSalesReport(monthStart, LocalDate.now(), null);
            model.addAttribute("salesByCategoryJson", toPieChartJson(pieData));
        } catch (Exception e) {
            model.addAttribute("salesByCategoryJson", "[]");
        }

        // 折线图 JSON
        try {
            List<Object[]> lineData = saleService.getDailySalesTrend(
                    LocalDate.now().minusDays(29), LocalDate.now());
            model.addAttribute("dailySalesJson", toLineChartJson(lineData));
        } catch (Exception e) {
            model.addAttribute("dailySalesJson", "[]");
        }

        // 营业额报表
        try {
            List<Map<String, Object>> salesReport = reportService.getSalesReport(startDate, endDate, categoryId);
            model.addAttribute("salesReport", salesReport);
        } catch (Exception e) {
            model.addAttribute("salesReport", java.util.Collections.EMPTY_LIST);
        }

        // 利润分析
        try {
            List<Map<String, Object>> profitReport = reportService.getProfitReport(startDate, endDate, categoryId);
            model.addAttribute("profitReport", profitReport);
        } catch (Exception e) {
            model.addAttribute("profitReport", java.util.Collections.EMPTY_LIST);
        }

        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("categories", categoryRepository.findAll());

        return "index";
    }

    private String toPieChartJson(List<Map<String, Object>> data) {
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (Map<String, Object> row : data) {
            if (!first) sb.append(",");
            first = false;
            String name = String.valueOf(row.get("categoryName"));
            Object amount = row.get("totalAmount");
            sb.append("{\"categoryName\":\"").append(escapeJson(name)).append("\",");
            sb.append("\"totalAmount\":").append(amount != null ? amount.toString() : "0");
            sb.append("}");
        }
        sb.append("]");
        return sb.toString();
    }

    private String toLineChartJson(List<Object[]> data) {
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (Object[] row : data) {
            if (!first) sb.append(",");
            first = false;
            sb.append("[\"").append(row[0] != null ? row[0].toString() : "").append("\",");
            sb.append(row[1] != null ? row[1].toString() : "0").append("]");
        }
        sb.append("]");
        return sb.toString();
    }

    private String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
