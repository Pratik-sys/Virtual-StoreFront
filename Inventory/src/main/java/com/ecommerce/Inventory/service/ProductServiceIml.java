package com.ecommerce.Inventory.service;

import com.ecommerce.Inventory.dto.ProductAddDTO;
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
    public ProductAddDTO addProducts(ProductAddDTO productAddDTO) {
        Product product = modelMapper.map(productAddDTO, Product.class);
        Product saveProduct = productRepository.save(product);
        return  modelMapper.map(saveProduct, ProductAddDTO.class);
    }

    @Override
    public List<Product> listAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public void deleteProductById(String id) {
        productRepository.deleteById(id);
    }
}
