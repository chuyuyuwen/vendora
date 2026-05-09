package com.city.vendora.Dao;

import com.city.vendora.entity.Purchase;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface PurchaseDao {
    int insert(Purchase purchase) throws SQLException;
    // 分页查询（支持日期范围、商品编号）
    List<Purchase> queryByCondition(String productId, Date startDate, Date endDate,
                                    int pageNo, int pageSize) throws SQLException;
    int countByCondition(String productId, Date startDate, Date endDate) throws SQLException;
    // 买入汇总（按商品性质、日期段统计进货总数量和总金额）
    List<Map<String, Object>> summaryByCategory(Date startDate, Date endDate) throws SQLException;
}
