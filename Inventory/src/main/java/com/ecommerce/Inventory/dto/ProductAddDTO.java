package com.ecommerce.Inventory.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductAddDTO {
        private  String id;
        private  String name;
        private BigDecimal price;
        private  int quantity;
    }
