package com.ecommerce.Inventory.service;

import com.ecommerce.Inventory.dto.ProductRequest;
import com.ecommerce.Inventory.dto.ProductResponse;
import com.ecommerce.Inventory.model.Product;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ecommerce.Inventory.repository.ProductRepository;

import java.util.List;

@Service
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class ProductServiceImpl implements  ProductService{

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ProductRequest addProducts(ProductRequest productRequest) {
        Product product = Product.builder()
                .name(productRequest.getName())
                .price(productRequest.getPrice())
                .description(productRequest.getDescription())
                .build();
        productRepository.save(product);
        log.info("Product added -> {}", product);
        return modelMapper.map(product, ProductRequest.class);
    }

    @Override
    public List<ProductResponse> listAllProducts() {
        List<Product> findProducts = productRepository.findAll();
        log.info("Fetched products -> {}", findProducts);
        return findProducts.stream().map(this::mapToProductResponse).toList();
    }

    @Override
    public void deleteProductById(String id) {
        log.info("Deleted the product with id {}", id);
        productRepository.deleteById(id);
    }

    private  ProductResponse mapToProductResponse(Product product){
       return  ProductResponse.builder()
               .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .build();
    }
}
