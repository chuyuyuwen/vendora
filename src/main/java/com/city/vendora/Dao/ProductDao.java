package com.city.vendora.Dao;

import com.city.vendora.entity.Product;

import java.sql.SQLException;
import java.util.List;

public interface ProductDao {
    int insert(Product product) throws SQLException;
    int update(Product product) throws SQLException;
    int delete(String productId) throws SQLException;       // 逻辑删除（置status=0）
    Product findById(String productId) throws SQLException;
    List<Product> findAll() throws SQLException;
    // 支持多条件模糊查询
    List<Product> findByCondition(String productId, String productName, Integer categoryId) throws SQLException;
}
