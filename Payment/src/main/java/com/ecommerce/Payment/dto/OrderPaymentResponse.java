package com.ecommerce.Payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderPaymentResponse {
    private String orderNumber;
    private List<String> p_id;
    private String paymentStatus;
    private BigDecimal totalAmount;


}
