package com.ecommerce.Inventory.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
        private  String name;
        private BigDecimal price;
        private String description;
    }
