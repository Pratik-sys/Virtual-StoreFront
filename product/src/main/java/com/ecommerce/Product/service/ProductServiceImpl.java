package com.ecommerce.Product.service;

import com.ecommerce.Product.dto.ProductRequest;
import com.ecommerce.Product.dto.ProductResponse;
import com.ecommerce.Product.dto.ProductResponseToEmail;
import com.ecommerce.Product.model.Product;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ecommerce.Product.repository.ProductRepository;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class ProductServiceImpl implements  ProductService{

    private static final Logger LOG = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ProductRequest addProducts(ProductRequest productRequest) {
        LOG.info("Building Product to save in database");
        Product product = Product.builder()
                .name(productRequest.getName())
                .price(productRequest.getPrice())
                .description(productRequest.getDescription())
                .build();
        productRepository.save(product);
        LOG.info("Product saved to database -> {}", product);
        return modelMapper.map(product, ProductRequest.class);
    }

    @Override
    public List<ProductResponse> listAllProducts() {
        LOG.info("Fetching all products.....");
        List<Product> findProducts = productRepository.findAll();
        LOG.info("Fetched products -> {}", findProducts);
        return findProducts.stream().map(value -> modelMapper.map(value, ProductResponse.class)).toList();
    }

    @Override
    public boolean deleteProductById(String id) {
        LOG.info("Finding the product with id {}", id);
        Optional<Product>  optionalProduct = productRepository.findById(id);
        if(optionalProduct.isEmpty()){
            LOG.error("No such product found with id: {}", id);
            return false;
        }
        Product product = optionalProduct.get();
        productRepository.delete(product);
        LOG.info("Product with id {} deleted successfully", id);
        return  true;
    }

    @Override
    public ProductResponseToEmail getProductById(String id) {
        LOG.info("Finding product by id: {}", id);
        Optional<Product> product = productRepository.findById(id);
        LOG.info("Product with id {} : {}", id, product);
        return product.map(value -> modelMapper.map(value, ProductResponseToEmail.class)).orElse(null);

    }
}
