package com.ecommerce.Payment.service;

import java.math.BigDecimal;

public interface PaymentService {

    boolean makePayment(String id, BigDecimal amount);
}
