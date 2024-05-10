package com.ecommerce.Order.dto;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderEvent {
    private String orderNumber;
    private List<String> p_id;
    private String paymentStatus;
    private BigDecimal totalAmount;
}
