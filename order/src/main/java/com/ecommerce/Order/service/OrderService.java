package com.ecommerce.Order.service;

import com.ecommerce.Order.dto.OrderRequest;

import java.util.HashMap;

public interface OrderService {
     String placeOrder(OrderRequest orderRequest);
     void updatePaymentStatus(HashMap<String, String> paymentResponse);
}
