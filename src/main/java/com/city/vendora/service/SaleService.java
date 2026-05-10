package com.city.vendora.service;

import com.city.vendora.entity.Sale;
import com.city.vendora.util.PageBean;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface SaleService {
    // 卖出录入（校库存 + 减库存，事务）
    boolean addSale(Sale sale);
    PageBean<Sale> querySales(String productId, Date startDate, Date endDate, int pageNo, int pageSize);
    List<Map<String, Object>> getSaleSummary(Date startDate, Date endDate);
}
