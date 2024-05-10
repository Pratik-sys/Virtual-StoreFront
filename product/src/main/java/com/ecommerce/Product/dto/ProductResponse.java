package com.ecommerce.Product.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private  String id;
    private  String name;
    private BigDecimal price;
    private  String description;
}
