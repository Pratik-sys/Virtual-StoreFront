package com.ecommerce.Inventory.service;

import com.ecommerce.Inventory.dto.ProductRequest;
import com.ecommerce.Inventory.dto.ProductResponse;

import java.util.List;

public interface ProductService {
    ProductRequest addProducts(ProductRequest productRequest);
    List<ProductResponse> listAllProducts();
    void deleteProductById(String id);
}
