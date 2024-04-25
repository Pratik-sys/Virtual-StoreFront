package com.ecommerce.Inventory.service;

import com.ecommerce.Inventory.dto.AddToCartDTO;
import com.ecommerce.Inventory.model.Cart;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.CartRepository;
import java.util.List;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class CartServiceImpl implements  CartService{

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public AddToCartDTO addToCart(AddToCartDTO addToCartDTO) {
        Cart cart = modelMapper.map(addToCartDTO, Cart.class);
        Cart saveCart = cartRepository.save(cart);
        return  modelMapper.map(saveCart, AddToCartDTO.class);
    }

    @Override
    public void deleteCartById(String id) {
        cartRepository.deleteById(id);
    }

    @Override
    public void PlaceOrder(String id) {
        List<Cart> cart = cartRepository.findAll();
        if (cart.isEmpty()){
            System.out.println("No cart with such Id found" + id);
        }
        else {
            //TODO
            // Kafka to send the cart to Order service to process further.
        }

    }
}
