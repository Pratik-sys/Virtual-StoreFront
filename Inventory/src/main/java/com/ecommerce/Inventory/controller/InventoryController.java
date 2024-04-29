package com.ecommerce.Inventory.controller;

import com.ecommerce.Inventory.service.InventoryService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/product/inventory")
@AllArgsConstructor
@NoArgsConstructor
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;
}
