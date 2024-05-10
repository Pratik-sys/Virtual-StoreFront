package com.ecommerce.Order.service;

import com.ecommerce.Order.dto.OrderRequest;

public interface OrderService {
     String placeOrder(OrderRequest orderRequest);
     void updatePaymentStatus(String orderNumber, String paymentStatus);
}
