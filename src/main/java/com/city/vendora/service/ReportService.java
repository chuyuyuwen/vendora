package com.city.vendora.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ReportService {
    // 按时间段、商品性质统计营业额
    List<Map<String, Object>> getSalesReport(LocalDate startDate, LocalDate endDate, Integer categoryId);
    // 详细的利润分析（若有成本字段：利润 = 售价 - 进价，可扩展）
    List<Map<String, Object>> getProfitReport(LocalDate startDate, LocalDate endDate, Integer categoryId);
}
