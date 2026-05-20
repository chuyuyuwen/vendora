package com.city.vendora.controller;

import com.city.vendora.repository.CategoryRepository;
import com.city.vendora.service.ReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

@Controller
@RequestMapping("/report")
public class ReportController {

    private final ReportService reportService;
    private final CategoryRepository categoryRepository;

    public ReportController(ReportService reportService, CategoryRepository categoryRepository) {
        this.reportService = reportService;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/sales")
    public String salesReport(@RequestParam(required = false) @DateTimeFormat(iso = DATE) LocalDate startDate,
                              @RequestParam(required = false) @DateTimeFormat(iso = DATE) LocalDate endDate,
                              @RequestParam(required = false) Integer categoryId,
                              Model model) {
        List<Map<String, Object>> report = reportService.getSalesReport(startDate, endDate, categoryId);
        model.addAttribute("report", report);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("categories", categoryRepository.findAll());
        return "report/sales";
    }

    @GetMapping("/profit")
    public String profitReport(@RequestParam(required = false) @DateTimeFormat(iso = DATE) LocalDate startDate,
                               @RequestParam(required = false) @DateTimeFormat(iso = DATE) LocalDate endDate,
                               @RequestParam(required = false) Integer categoryId,
                               Model model) {
        List<Map<String, Object>> report = reportService.getProfitReport(startDate, endDate, categoryId);
        model.addAttribute("report", report);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("categories", categoryRepository.findAll());
        return "report/profit";
    }
}
