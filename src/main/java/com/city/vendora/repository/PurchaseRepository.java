package com.city.vendora.repository;

import com.city.vendora.entity.Purchase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Integer> {
    // 分页 + 按商品/日期筛选
    @Query("SELECT p FROM Purchase p WHERE " +
            "(:productId IS NULL OR p.product.productId = :productId) AND " +
            "(:startDate IS NULL OR p.purchaseDate >= :startDate) AND " +
            "(:endDate IS NULL OR p.purchaseDate <= :endDate)")
    Page<Purchase> findByCondition(@Param("productId") String productId,
                                   @Param("startDate") LocalDate startDate,
                                   @Param("endDate") LocalDate endDate,
                                   Pageable pageable);

    // 按商品性质统计进货汇总
    @Query("SELECT p.product.category.categoryName AS categoryName, " +
            "SUM(p.quantity) AS totalQuantity, SUM(p.totalAmount) AS totalAmount " +
            "FROM Purchase p " +
            "WHERE (:startDate IS NULL OR p.purchaseDate >= :startDate) " +
            "AND (:endDate IS NULL OR p.purchaseDate <= :endDate) " +
            "AND (:categoryId IS NULL OR p.product.category.id = :categoryId) " +
            "GROUP BY p.product.category.categoryName")
    List<Object[]> summaryByCategory(@Param("startDate") LocalDate startDate,
                                     @Param("endDate") LocalDate endDate,
                                     @Param("categoryId") Integer categoryId);
}
