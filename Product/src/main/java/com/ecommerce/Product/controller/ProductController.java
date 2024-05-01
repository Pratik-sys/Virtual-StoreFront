package com.ecommerce.Product.controller;

import com.ecommerce.Product.dto.ProductRequest;
import com.ecommerce.Product.dto.ProductResponse;
import com.ecommerce.Product.dto.ProductResponseToEmail;
import com.ecommerce.Product.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("api/product/")
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/list-all")
    public ResponseEntity<List<ProductResponse>> listAllProducts(){
        List<ProductResponse> productResponses = productService.listAllProducts();
        return ResponseEntity.ok().body(productResponses);
    }

    @PostMapping("/add")
    public ResponseEntity<ProductRequest> addProducts(@RequestBody ProductRequest productRequest){
        log.info("Received Request Body {}", productRequest);
        return ResponseEntity.ok().body(productService.addProducts(productRequest));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProductById (@PathVariable("id") String id ){
        log.info("Received the id to delete product {}", id);
        productService.deleteProductById(id);
        return ResponseEntity.ok().body(String.format("Product removed from database with id : %s",id));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseToEmail> getProductById(@PathVariable("id") String id ){
        log.info("Received the id to fetch the product {}", id);
        return ResponseEntity.ok().body(productService.getProductById(id));
    }


}
