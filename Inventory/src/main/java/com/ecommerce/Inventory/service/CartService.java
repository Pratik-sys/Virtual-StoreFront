package com.ecommerce.Inventory.service;

import com.ecommerce.Inventory.model.Cart;

public interface CartService {
    Cart addToCart(Cart cart);
    void deleteCartById(String id);
}
