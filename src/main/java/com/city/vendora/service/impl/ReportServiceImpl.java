package com.city.vendora.service.impl;

import com.city.vendora.repository.PurchaseRepository;
import com.city.vendora.repository.SaleRepository;
import com.city.vendora.service.ReportService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {
    private final SaleRepository saleRepository;
    private final PurchaseRepository purchaseRepository;

    public ReportServiceImpl(SaleRepository saleRepository, PurchaseRepository purchaseRepository) {
        this.saleRepository = saleRepository;
        this.purchaseRepository = purchaseRepository;
    }

    @Override
    public List<Map<String, Object>> getSalesReport(LocalDate startDate, LocalDate endDate, Integer categoryId) {
        List<Object[]> results = saleRepository.salesSummary(startDate, endDate, categoryId);
        List<Map<String, Object>> list = new ArrayList<>();
        for (Object[] row : results) {
            Map<String, Object> map = new HashMap<>();
            map.put("categoryName", row[0]);
            map.put("totalQuantity", row[1]);
            map.put("totalAmount", row[2]);
            list.add(map);
        }
        return list;
    }

    @Override
    public List<Map<String, Object>> getProfitReport(LocalDate startDate, LocalDate endDate, Integer categoryId) {
        List<Map<String, Object>> list = new ArrayList<>();

        List<Object[]> salesData = saleRepository.salesSummary(startDate, endDate, categoryId);
        List<Object[]> purchaseData = purchaseRepository.summaryByCategory(startDate, endDate, categoryId);

        // 将进货数据按性质名称放入 Map，便于匹配
        Map<String, Object[]> purchaseMap = new HashMap<>();
        for (Object[] p : purchaseData) {
            purchaseMap.put((String) p[0], p);
        }

        for (Object[] s : salesData) {
            String catName = (String) s[0];
            Map<String, Object> map = new HashMap<>();
            map.put("categoryName", catName);
            map.put("saleQuantity", s[1]);
            map.put("saleAmount", s[2]);

            Object[] p = purchaseMap.get(catName);
            BigDecimal purchaseAmount = p != null ? (BigDecimal) p[2] : BigDecimal.ZERO;
            map.put("purchaseAmount", purchaseAmount);
            // 利润 = 销售额 - 进货成本
            map.put("profit", ((BigDecimal) s[2]).subtract(purchaseAmount));
            list.add(map);
        }
        return list;
    }
}
