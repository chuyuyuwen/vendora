package com.city.vendora.service.impl;

import com.city.vendora.entity.Product;
import com.city.vendora.repository.ProductRepository;
import com.city.vendora.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public boolean addProduct(Product product) {
        productRepository.save(product);
        return true;
    }

    @Override
    public boolean modifyProduct(Product product) {
        productRepository.save(product);
        return true;
    }

    @Override
    public boolean disableProduct(String productId) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product != null) {
            product.setStatus(0);
            productRepository.save(product);
            return true;
        }
        return false;
    }

    @Override
    public Product getProductById(String productId) {
        return productRepository.findById(productId).orElse(null);
    }

    @Override
    public List<Product> listAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> searchProducts(String productId, String productName, Integer categoryId) {
        if (categoryId != null) {
            return productRepository.findByCategory_Id(categoryId);
        }
        return productRepository.findByProductIdContainingAndProductNameContaining(
                productId != null ? productId : "",
                productName != null ? productName : "");
    }
}
