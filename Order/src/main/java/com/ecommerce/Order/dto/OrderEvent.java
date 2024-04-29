package com.ecommerce.Order.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderEvent {
    private  long orderId;
    private String orderNumber;
    private List<String> p_id;
}
