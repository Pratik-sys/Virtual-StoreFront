package com.ecommerce.Inventory.controller;

import com.ecommerce.Inventory.dto.ProductDTO;
import com.ecommerce.Inventory.model.Product;
import com.ecommerce.Inventory.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/product/")
@RequiredArgsConstructor
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/list-all")
    public ResponseEntity<List<Product>> listAllProducts(){
        return ResponseEntity.ok().body(productService.listAllProducts());
    }

    @PostMapping("/add")
    public ResponseEntity<ProductDTO> addProducts(@RequestBody ProductDTO productDTO){
        return ResponseEntity.ok().body(productService.addProducts(productDTO));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProductById (@PathVariable("id") String id ){
        productService.deleteProductById(id);
        return ResponseEntity.ok().body("Product with id " + id + "deleted");
    }


}
