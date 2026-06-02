package com.city.vendora.service.impl;

import com.city.vendora.entity.Inventory;
import com.city.vendora.entity.Purchase;
import com.city.vendora.repository.InventoryRepository;
import com.city.vendora.repository.PurchaseRepository;
import com.city.vendora.service.PurchaseService;
import com.city.vendora.util.PageBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PurchaseServiceImpl implements PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final InventoryRepository inventoryRepository;

    public PurchaseServiceImpl(PurchaseRepository purchaseRepository, InventoryRepository inventoryRepository) {
        this.purchaseRepository = purchaseRepository;
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    @Transactional
    public boolean addPurchase(Purchase purchase) {
        // 自动计算总金额
        if (purchase.getTotalAmount() == null) {
            purchase.setTotalAmount(
                    purchase.getPurchasePrice().multiply(BigDecimal.valueOf(purchase.getQuantity())));
        }
        purchaseRepository.save(purchase);

        // 更新库存
        String productId = purchase.getProduct().getProductId();
        Inventory inventory = inventoryRepository.findById(productId).orElse(null);
        if (inventory != null) {
            inventory.setQuantity(inventory.getQuantity() + purchase.getQuantity());
        } else {
            inventory = new Inventory();
            inventory.setProduct(purchase.getProduct());
            inventory.setQuantity(purchase.getQuantity());
        }
        inventoryRepository.save(inventory);
        return true;
    }

    @Override
    public PageBean<Purchase> queryPurchases(String productId, LocalDate startDate, LocalDate endDate, int pageNo, int
            pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        Page<Purchase> page = purchaseRepository.findByCondition(productId, startDate, endDate, pageable);
        return new PageBean<>(pageNo, pageSize, (int) page.getTotalElements(), page.getContent());
    }

    @Override
    public List<Map<String, Object>> getPurchaseSummary(LocalDate startDate, LocalDate endDate) {
        List<Object[]> results = purchaseRepository.summaryByCategory(startDate, endDate, null);
        List<Map<String, Object>> list = new ArrayList<>();
        for (Object[] row : results) {
            Map<String, Object> map = new HashMap<>();
            map.put("categoryName", row[0]);
            map.put("totalQuantity", row[1]);
            map.put("totalAmount", row[2]);
            list.add(map);
        }
        return list;
    }

    @Override
    // Dashboard：获取今日买入总额
    public BigDecimal getTodayPurchasesAmount() {
        return purchaseRepository.sumTotalAmountByPurchaseDate(LocalDate.now());
    }

    @Override
    // Dashboard：获取今日买入笔数
    public long getTodayPurchasesCount() {
        return purchaseRepository.countByPurchaseDate(LocalDate.now());
    }
}
