package com.ecommerce.Inventory.service;

import com.ecommerce.Inventory.dto.ProductDTO;
import com.ecommerce.Inventory.model.Product;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ecommerce.Inventory.repository.ProductRepository;

import java.util.List;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class ProductServiceImpl implements  ProductService{

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ProductDTO addProducts(ProductDTO productDTO) {
        Product product = modelMapper.map(productDTO, Product.class);
        Product saveProduct = productRepository.save(product);
        return  modelMapper.map(saveProduct, ProductDTO.class);
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
