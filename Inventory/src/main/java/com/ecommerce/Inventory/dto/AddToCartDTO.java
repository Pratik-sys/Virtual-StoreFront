package com.ecommerce.Inventory.dto;

import com.ecommerce.Inventory.model.Product;
import lombok.Data;
import java.util.Date;

@Data
public class AddToCartDTO {
    private  String id;
    private Product product;
    private final Date timeStamp =  new Date();
}
