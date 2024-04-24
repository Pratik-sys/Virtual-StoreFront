package com.ecommerce.Inventory.controller;

import com.ecommerce.Inventory.dto.ProductAddDTO;
import com.ecommerce.Inventory.model.Product;
import com.ecommerce.Inventory.service.ProductService;
import com.ecommerce.Inventory.service.ProductServiceIml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/product/")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/list-all")
    public ResponseEntity<List<Product>> listAllProducts(){
        return ResponseEntity.ok().body(productService.listAllProducts());
    }

    @PostMapping("/add")
    public ResponseEntity<ProductAddDTO> addProducts(ProductAddDTO productAddDTO){
        return ResponseEntity.ok().body(productService.addProducts(productAddDTO));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProductById (@PathVariable("id") String id ){
        productService.deleteProductById(id);
        return ResponseEntity.ok().body("Product with id " + id + "deleted");
    }

}
