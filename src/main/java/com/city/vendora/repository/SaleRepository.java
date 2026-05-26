package com.city.vendora.repository;

import com.city.vendora.entity.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale,Integer> {
    @Query("SELECT s FROM Sale s WHERE " +
            "(:productId IS NULL OR s.product.productId = :productId) AND " +
            "(:startDate IS NULL OR s.saleDate >= :startDate) AND " +
            "(:endDate IS NULL OR s.saleDate <= :endDate)")
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
    List<Object[]> salesSummary(@Param("startDate") LocalDate startDate,
                                @Param("endDate") LocalDate endDate,
                                @Param("categoryId") Integer categoryId);
}
