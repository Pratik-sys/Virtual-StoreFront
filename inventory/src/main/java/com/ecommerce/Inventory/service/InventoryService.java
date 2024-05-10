package com.ecommerce.Inventory.service;

import com.ecommerce.Inventory.dto.InventoryUpdate;
import com.ecommerce.Inventory.dto.OrderInventoryResponse;

import java.util.List;

public interface InventoryService {
    List<OrderInventoryResponse> checkStock(List<String> p_id);
    InventoryUpdate addProductsToInventory(InventoryUpdate inventoryUpdate);
}
