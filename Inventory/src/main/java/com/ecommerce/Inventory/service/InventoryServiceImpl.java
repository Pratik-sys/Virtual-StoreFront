package com.ecommerce.Inventory.service;

import com.ecommerce.Inventory.dto.InventoryUpdate;
import com.ecommerce.Inventory.dto.OrderInventoryResponse;
import com.ecommerce.Inventory.model.Inventory;
import com.ecommerce.Inventory.repository.InventoryRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class InventoryServiceImpl implements InventoryService{

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<OrderInventoryResponse> checkStock(List<String> p_id) {
        log.info("checking stock of product ID's: {}", p_id);
        return  inventoryRepository.findByProductIdIn(p_id).stream().map(
                inventory -> OrderInventoryResponse.builder()
                        .pId(inventory.getProductId())
                        .is_inStock(inventory.getQuantity() > 0)
                        .build()).toList();
    }

    @Override
    public InventoryUpdate addProductsToInventory(InventoryUpdate inventoryUpdate) {
        log.info("Adding products to Inventory");
        Inventory inventory = Inventory.builder()
                .productId(inventoryUpdate.getP_id())
                .quantity(inventoryUpdate.getQuantity())
                .build();
        inventoryRepository.save(inventory);
        log.info("Saved inventory:{}", inventory);
        return  modelMapper.map(inventory, InventoryUpdate.class);
    }
}
