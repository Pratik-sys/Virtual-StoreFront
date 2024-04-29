package com.ecommerce.Product.controller;

import com.ecommerce.Product.dto.ProductRequest;
import com.ecommerce.Product.dto.ProductResponse;
import com.ecommerce.Product.dto.ProductResponseToEmail;
import com.ecommerce.Product.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/product/")
@AllArgsConstructor
@NoArgsConstructor
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/list-all")
    public ResponseEntity<List<ProductResponse>> listAllProducts(){
        return ResponseEntity.ok().body(productService.listAllProducts());
    }

    @PostMapping("/add")
    public ResponseEntity<ProductRequest> addProducts(@RequestBody ProductRequest productRequest){
        return ResponseEntity.ok().body(productService.addProducts(productRequest));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProductById (@PathVariable("id") String id ){
        productService.deleteProductById(id);
        return ResponseEntity.ok().body("Product with id " + id + "deleted");
    }
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseToEmail> getProductById(@PathVariable("id") String id ){
        return ResponseEntity.ok().body(productService.getProductById(id));
    }


}
