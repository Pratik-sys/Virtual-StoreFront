package com.ecommerce.Inventory.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderInventoryResponse {
    private  String pId;
    private  boolean  is_inStock;
}
