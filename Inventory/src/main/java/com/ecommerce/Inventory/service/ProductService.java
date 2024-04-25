package com.ecommerce.Inventory.service;

import com.ecommerce.Inventory.dto.ProductAddDTO;
import com.ecommerce.Inventory.model.Product;
import java.util.List;

public interface ProductService {
    ProductAddDTO addProducts(ProductAddDTO productAddDTO);
    List<Product> listAllProducts();
    void deleteProductById(String id);
}
