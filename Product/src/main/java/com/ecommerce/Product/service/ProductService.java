package com.ecommerce.Product.service;

import com.ecommerce.Product.dto.ProductRequest;
import com.ecommerce.Product.dto.ProductResponse;

import java.util.List;

public interface ProductService {
    ProductRequest addProducts(ProductRequest productRequest);
    List<ProductResponse> listAllProducts();
    void deleteProductById(String id);
}
