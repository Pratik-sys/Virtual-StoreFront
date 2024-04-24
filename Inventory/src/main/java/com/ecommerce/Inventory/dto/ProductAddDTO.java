package com.ecommerce.Inventory.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
@Data
public class ProductAddDTO {
        @Id
        private  String id;
        private  String name;
        private BigDecimal price;
        private  int quantity;
    }
