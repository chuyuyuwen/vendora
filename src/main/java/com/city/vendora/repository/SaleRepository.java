package com.city.vendora.repository;

import com.city.vendora.entity.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale,Integer> {
    @Query("SELECT s FROM Sale s WHERE " +
            "(:productId IS NULL OR s.product.productId = :productId) AND " +
            "(:startDate IS NULL OR s.saleDate >= :startDate) AND " +
            "(:endDate IS NULL OR s.saleDate <= :endDate) " +
            "ORDER BY s.saleDate DESC, s.id DESC")
    Page<Sale> findByCondition(@Param("productId") String productId,
                               @Param("startDate") LocalDate startDate,
                               @Param("endDate") LocalDate endDate,
                               Pageable pageable);

    @Query("SELECT s.product.category.categoryName AS categoryName, " +
            "SUM(s.quantity) AS totalQuantity, SUM(s.totalAmount) AS totalAmount " +
            "FROM Sale s " +
            "WHERE (:startDate IS NULL OR s.saleDate >= :startDate) " +
            "AND (:endDate IS NULL OR s.saleDate <= :endDate) " +
            "AND (:categoryId IS NULL OR s.product.category.id = :categoryId) " +
            "GROUP BY s.product.category.categoryName")
    // 按商品性质统计销售额汇总
    List<Object[]> salesSummary(@Param("startDate") LocalDate startDate,
                                @Param("endDate") LocalDate endDate,
                                @Param("categoryId") Integer categoryId);

    // Dashboard：查询指定日期的销售总金额
    @Query("SELECT COALESCE(SUM(s.totalAmount), 0) FROM Sale s WHERE s.saleDate = :date")
    BigDecimal sumTotalAmountBySaleDate(@Param("date") LocalDate date);

    // Dashboard：查询指定日期的销售笔数
    @Query("SELECT COUNT(s) FROM Sale s WHERE s.saleDate = :date")
    long countBySaleDate(@Param("date") LocalDate date);

    // Dashboard：查询日期范围内每日销售额（给折线图用）
    @Query("SELECT s.saleDate, COALESCE(SUM(s.totalAmount), 0) FROM Sale s " +
            "WHERE s.saleDate BETWEEN :start AND :end " +
            "GROUP BY s.saleDate ORDER BY s.saleDate")
    List<Object[]> dailySalesTotal(@Param("start") LocalDate start,
                                   @Param("end") LocalDate end);

    Sale findTopByProductProductIdOrderBySaleDateDesc(String productId);
}
