package com.ecommerce.Inventory.controller;

import com.ecommerce.Inventory.dto.InventoryUpdate;
import com.ecommerce.Inventory.dto.OrderInventoryResponse;
import com.ecommerce.Inventory.service.InventoryService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/product/inventory")
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @PostMapping("/add")
    public ResponseEntity<InventoryUpdate> addProductToInventory(@RequestBody InventoryUpdate inventoryUpdate){
        log.info("Received request body {} to update inventory", inventoryUpdate);
        return ResponseEntity.ok().body(inventoryService.addProductsToInventory(inventoryUpdate));
    }
    @GetMapping("/check-stock")
    public List<OrderInventoryResponse> isInStock(@RequestParam List<String> p_id){
        log.info("Received stock check  request for product id: {}", p_id);
       return inventoryService.checkStock(p_id);
    }

}

