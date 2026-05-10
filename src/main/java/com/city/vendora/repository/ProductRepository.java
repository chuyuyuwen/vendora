package com.city.vendora.repository;

import com.city.vendora.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProductRepository extends JpaRepository<Product,String> {
    // 按商品编号模糊查询、按名称模糊查询、按类别筛选
    List<Product> findByProductIdContainingAndProductNameContaining(
            String productId, String productName);

    List<Product> findByCategory_Id(Integer categoryId);
}
