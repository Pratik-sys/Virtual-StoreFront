package com.ecommerce.Inventory.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
@Data
public class Cart {
    @Id
    private  String id;
    private  Product product;
    private  Date timeStamp =  new Date();
}
