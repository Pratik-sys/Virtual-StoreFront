package com.ecommerce.Inventory.service;

import com.ecommerce.Inventory.dto.AddToCartDTO;
import com.ecommerce.Inventory.model.Cart;
import com.ecommerce.Inventory.model.Product;
import com.ecommerce.Inventory.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ecommerce.Inventory.repository.CartRepository;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class CartServiceImpl implements  CartService{

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public AddToCartDTO addToCart(String pid) {
        Optional<Product> searchWithID = productRepository.findById(pid);
        if(searchWithID.isPresent()){
            Cart buildCart = new Cart();
            buildCart.setProduct(searchWithID.get());
            cartRepository.save(buildCart);
            return modelMapper.map(buildCart, AddToCartDTO.class);
        }
        else {
            return null;
        }

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
