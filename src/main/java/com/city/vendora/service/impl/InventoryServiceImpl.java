package com.city.vendora.service.impl;

import com.city.vendora.entity.Inventory;
import com.city.vendora.repository.InventoryRepository;
import com.city.vendora.service.InventoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepository inventoryRepository;

    public InventoryServiceImpl(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public List<Inventory> getInventoryList(String productId, Integer categoryId) {
        if (productId != null && !productId.isEmpty()) {
            return inventoryRepository.findById(productId)
                    .map(List::of).orElse(List.of());
        }
        if (categoryId != null) {
            return inventoryRepository.findByCategory(categoryId);
        }
        return inventoryRepository.findAll();
    }

    @Override
    public boolean isStockEnough(String productId, int requiredQty) {
        return inventoryRepository.getStock(productId) >= requiredQty;
    }

    @Override
    public List<Inventory> getLowStockProducts(int threshold) {
        return inventoryRepository.findByQuantityLessThan(threshold);
    }
}
