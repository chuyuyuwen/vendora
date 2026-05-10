package com.city.vendora.Dao;

import com.city.vendora.entity.Inventory;

import java.sql.SQLException;
import java.util.List;

public interface InventoryDao {
    // 查询当前库存（支持按商品编号、商品性质筛选，可扩展为分页）
    List<Inventory> queryInventory(String productId, Integer categoryId) throws SQLException;
    // 直接获取某商品的库存数量
    int getStock(String productId) throws SQLException;
    // 增加库存（买入时调用）
    int increaseStock(String productId, int quantity) throws SQLException;
    // 减少库存（卖出时调用）
    int decreaseStock(String productId, int quantity) throws SQLException;
    // 初始化库存记录（新增商品时调用）
    int initStock(String productId) throws SQLException;
}
