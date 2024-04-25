package com.ecommerce.Inventory.service;

import com.ecommerce.Inventory.dto.AddToCartDTO;

public interface CartService {
    AddToCartDTO addToCart(AddToCartDTO addToCartDTO);
    void deleteCartById(String id);
    void PlaceOrder(String id);
}
