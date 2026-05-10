package com.city.vendora.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ReportService {
    // 按时间段、商品性质统计营业额
    List<Map<String, Object>> getSalesReport(Date startDate, Date endDate, Integer categoryId);
    // 详细的利润分析（若有成本字段：利润 = 售价 - 进价，可扩展）
    List<Map<String, Object>> getProfitReport(Date startDate, Date endDate, Integer categoryId);
}
