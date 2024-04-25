package com.ecommerce.Inventory.service;

import com.ecommerce.Inventory.dto.ProductDTO;
import com.ecommerce.Inventory.model.Product;
import java.util.List;

public interface ProductService {
    ProductDTO addProducts(ProductDTO productDTO);
    List<Product> listAllProducts();
    void deleteProductById(String id);
}
