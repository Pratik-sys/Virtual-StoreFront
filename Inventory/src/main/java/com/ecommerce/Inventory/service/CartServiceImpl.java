package com.ecommerce.Inventory.service;

import com.ecommerce.Inventory.model.Cart;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class CartServiceImpl implements  CartService{

    @Autowired
    private  CartServiceImpl cartService;
    
    @Override
    public Cart addToCart(Cart cart) {
        return null;
    }

    @Override
    public void deleteCartById(String id) {

    }
}
