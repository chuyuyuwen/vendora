package com.city.vendora.service;

import com.city.vendora.entity.Inventory;

import java.util.List;

public interface InventoryService {
    List<Inventory> getInventoryList(String productId, Integer categoryId);
    // 检查库存是否充足
    boolean isStockEnough(String productId, int requiredQty);
    // 低库存预警（如库存 < 10）
    List<Inventory> getLowStockProducts(int threshold);
}
