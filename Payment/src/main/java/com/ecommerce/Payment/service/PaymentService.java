package com.ecommerce.Payment.service;

import java.math.BigDecimal;

public interface PaymentService {

    void makePayment(String id, BigDecimal amount);
}
