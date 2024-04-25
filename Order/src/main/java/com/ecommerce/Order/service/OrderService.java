package com.ecommerce.Order.service;

import com.ecommerce.Order.model.Order;

import java.util.List;

public interface OrderService {
    void deleteOrderByID(String id);
    void confirmOrder();
    List<Order> listAllOrders();
}
