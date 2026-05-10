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
}
