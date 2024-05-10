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
public class ConfirmPaymentResponse {
    private String paymentId;
    private String paymentStatus;
    private String orderNumber;
    private BigDecimal totalAmount;
    private List<String> p_id;
}
