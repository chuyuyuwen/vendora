package com.city.vendora.service.impl;

import com.city.vendora.entity.Inventory;
import com.city.vendora.entity.Sale;
import com.city.vendora.repository.InventoryRepository;
import com.city.vendora.repository.SaleRepository;
import com.city.vendora.service.SaleService;
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
public class SaleServiceImpl  implements SaleService {
    private final SaleRepository saleRepository;
    private final InventoryRepository inventoryRepository;

    public SaleServiceImpl(SaleRepository saleRepository, InventoryRepository inventoryRepository) {
        this.saleRepository = saleRepository;
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    @Transactional
    public boolean addSale(Sale sale) {
        String productId = sale.getProduct().getProductId();

        // 校验库存
        int stock = inventoryRepository.getStock(productId);
        if (stock < sale.getQuantity()) {
            return false;
        }

        // 自动计算总金额
        if (sale.getTotalAmount() == null) {
            sale.setTotalAmount(
                    sale.getSalePrice().multiply(BigDecimal.valueOf(sale.getQuantity())));
        }
        saleRepository.save(sale);

        // 扣库存
        Inventory inventory = inventoryRepository.findById(productId).orElse(null);
        if (inventory != null) {
            inventory.setQuantity(inventory.getQuantity() - sale.getQuantity());
            inventoryRepository.save(inventory);
        }
        return true;
    }

    @Override
    public PageBean<Sale> querySales(String productId, LocalDate startDate, LocalDate endDate, int pageNo, int
            pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        Page<Sale> page = saleRepository.findByCondition(productId, startDate, endDate, pageable);
        return new PageBean<>(pageNo, pageSize, (int) page.getTotalElements(), page.getContent());
    }

    @Override
    public List<Map<String, Object>> getSaleSummary(LocalDate startDate, LocalDate endDate) {
        List<Object[]> results = saleRepository.salesSummary(startDate, endDate, null);
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
}
