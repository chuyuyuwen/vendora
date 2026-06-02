package com.city.vendora.service;

import com.city.vendora.entity.Product;

import java.util.List;

public interface ProductService {
    boolean addProduct(Product product);
    boolean modifyProduct(Product product);
    boolean disableProduct(String productId);
    Product getProductById(String productId);
    List<Product> listAllProducts();
    List<Product> searchProducts(String productId, String productName, Integer categoryId);
    // 获取商品总数（给 dashboard 首页用）
    long countAllProducts();
    // 获取已停用商品数
    long countDisabledProducts();
}
