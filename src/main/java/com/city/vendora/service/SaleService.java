package com.city.vendora.service;

import com.city.vendora.entity.Sale;
import com.city.vendora.util.PageBean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface SaleService {
    // 卖出录入（校库存 + 减库存，事务）
    boolean addSale(Sale sale);
    PageBean<Sale> querySales(String productId, LocalDate startDate, LocalDate endDate, int pageNo, int pageSize);
    // 按日期范围汇总卖出记录
    List<Map<String, Object>> getSaleSummary(LocalDate startDate, LocalDate endDate);
    // Dashboard：获取今日销售总额
    BigDecimal getTodaySalesAmount();
    // Dashboard：获取今日销售笔数
    long getTodaySalesCount();
    // Dashboard：获取日期范围内的每日销售额趋势
    List<Object[]> getDailySalesTrend(LocalDate startDate, LocalDate endDate);
}
