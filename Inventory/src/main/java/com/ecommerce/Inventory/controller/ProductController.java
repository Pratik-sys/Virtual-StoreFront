package com.ecommerce.Inventory.controller;

import com.ecommerce.Inventory.dto.AddToCartDTO;
import com.ecommerce.Inventory.dto.ProductAddDTO;
import com.ecommerce.Inventory.model.Product;
import com.ecommerce.Inventory.service.CartService;
import com.ecommerce.Inventory.service.ProductService;
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

    @Autowired
    private CartService cartService;

    @GetMapping("/list-all")
    public ResponseEntity<List<Product>> listAllProducts(){
        return ResponseEntity.ok().body(productService.listAllProducts());
    }

    @PostMapping("/add")
    public ResponseEntity<ProductAddDTO> addProducts(@RequestBody ProductAddDTO productAddDTO){
        return ResponseEntity.ok().body(productService.addProducts(productAddDTO));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProductById (@PathVariable("id") String id ){
        productService.deleteProductById(id);
        return ResponseEntity.ok().body("Product with id " + id + "deleted");
    }
    @PostMapping("/add-to-cart/{pid}")
    public ResponseEntity<AddToCartDTO> addToCart (@PathVariable("pid") String pid){
        return ResponseEntity.ok().body(cartService.addToCart(pid));
    }
    @DeleteMapping("/delete-Cart/{id})")
    public  ResponseEntity<String> deleteCartById (@PathVariable("id") String id){
        cartService.deleteCartById(id);
        return ResponseEntity.ok().body("Cart with id : " + id  + "is deleted");
    }
    @PostMapping("/place-order/{id}")
        public ResponseEntity<String> placeOrder(@PathVariable("id") String id){
        cartService.PlaceOrder(id);
        return  ResponseEntity.ok().body("Processing the order");
    }

}
