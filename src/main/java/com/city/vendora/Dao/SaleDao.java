package com.city.vendora.Dao;

import com.city.vendora.entity.Sale;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface SaleDao {
    int insert(Sale sale) throws SQLException;
    // 分页查询卖出记录
    List<Sale> queryByCondition(String productId, Date startDate, Date endDate,
                                int pageNo, int pageSize) throws SQLException;
    int countByCondition(String productId, Date startDate, Date endDate) throws SQLException;
    // 营业报表用汇总：按时间段、商品性质统计销售总额、销售量等
    List<Map<String, Object>> salesSummary(Date startDate, Date endDate, Integer categoryId) throws SQLException;
}
