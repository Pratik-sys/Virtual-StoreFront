package com.ecommerce.Email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {
    private String paymentId;
    private String paymentStatus;
    private  String orderNumber;
    private BigDecimal totalAmount;
    private List<String> p_id;

}
