package com.ecommerce.Order.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderEvent {
    private  long orderId;
    private String orderNumber;
}
