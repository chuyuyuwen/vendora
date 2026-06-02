package com.city.vendora.service;

import com.city.vendora.entity.Purchase;
import com.city.vendora.util.PageBean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface PurchaseService {
    // 买入录入（含库存增加，事务）
    boolean addPurchase(Purchase purchase);
    // 查询（返回分页对象）
    PageBean<Purchase> queryPurchases(String productId, LocalDate startDate, LocalDate endDate, int pageNo, int pageSize);
    // 汇总数据，用于打印
    List<Map<String, Object>> getPurchaseSummary(LocalDate startDate, LocalDate endDate);
    // Dashboard：获取今日买入总额
    BigDecimal getTodayPurchasesAmount();
    // Dashboard：获取今日买入笔数
    long getTodayPurchasesCount();
}
