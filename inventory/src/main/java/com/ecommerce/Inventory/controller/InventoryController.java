package com.ecommerce.Inventory.controller;

import com.ecommerce.Inventory.dto.InventoryUpdate;
import com.ecommerce.Inventory.dto.OrderInventoryResponse;
import com.ecommerce.Inventory.service.InventoryService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/product/inventory")
@AllArgsConstructor
@NoArgsConstructor
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @PostMapping("/add")
    public ResponseEntity<InventoryUpdate> addProductToInventory(@RequestBody InventoryUpdate inventoryUpdate){
        return ResponseEntity.ok().body(inventoryService.addProductsToInventory(inventoryUpdate));
    }
    @GetMapping("/check-stock")
    public List<OrderInventoryResponse> isInStock(@RequestParam List<String> p_id){
       return inventoryService.checkStock(p_id);
    }

}

