package com.ecommerce.Inventory.service;

import com.ecommerce.Inventory.dto.AddToCartDTO;

public interface CartService {
    AddToCartDTO addToCart(String pid);
    void deleteCartById(String id);
    void PlaceOrder(String id);
}
