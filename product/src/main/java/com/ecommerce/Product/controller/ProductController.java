package com.ecommerce.Product.controller;

import com.ecommerce.Product.dto.ProductRequest;
import com.ecommerce.Product.dto.ProductResponse;
import com.ecommerce.Product.dto.ProductResponseToEmail;
import com.ecommerce.Product.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        List<ProductResponse> productResponses = productService.listAllProducts();
        return ResponseEntity.ok().body(productResponses);
    }

    @PostMapping("/add")
    public ResponseEntity<ProductRequest> addProducts(@RequestBody ProductRequest productRequest){
        return ResponseEntity.ok().body(productService.addProducts(productRequest));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProductById (@PathVariable("id") String id ){
        boolean productRemoved = productService.deleteProductById(id);
        if(productRemoved){
            return ResponseEntity.ok().body(String.format("Product Removed with id: %s", id));
        }
        else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Request failed: Invalid id provided");
    }
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseToEmail> getProductById(@PathVariable("id") String id ){
        return ResponseEntity.ok().body(productService.getProductById(id));
    }


}
