package com.ecommerce.Inventory.service;

import com.ecommerce.Inventory.model.Product;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import repository.ProductRepository;

import java.util.List;

public class ProductServiceIml implements  ProductService{

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Product addProducts(Product product) {
        return null;
    }

    @Override
    public List<Product> listAllProducts() {
        return List.of();
    }

    @Override
    public void deleteProductById(String id) {
    }
}
