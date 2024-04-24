package com.ecommerce.Inventory.service;

import com.ecommerce.Inventory.model.Product;

import java.util.List;

public interface ProductService {
    Product addProducts(Product product);
    List<Product> listAllProducts();
    void deleteProductById(String id);
}
