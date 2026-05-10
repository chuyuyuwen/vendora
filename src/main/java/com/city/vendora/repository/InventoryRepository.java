package com.city.vendora.repository;

import com.city.vendora.entity.Inventory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository {
    // 按商品性质筛选库存
    @Query("SELECT i FROM Inventory i WHERE (:categoryId IS NULL OR i.product.category.id = :categoryId)")
    List<Inventory> findByCategory(@Param("categoryId") Integer categoryId);

    // 低库存预警
    List<Inventory> findByQuantityLessThan(int threshold);

    // 查某商品库存量
    @Query("SELECT i.quantity FROM Inventory i WHERE i.productId = :productId")
    int getStock(@Param("productId") String productId);
}
