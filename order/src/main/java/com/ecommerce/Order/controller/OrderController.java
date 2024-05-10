package com.ecommerce.Order.controller;

import com.ecommerce.Order.dto.OrderRequest;
import com.ecommerce.Order.service.OrderService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product/order")
@AllArgsConstructor
@NoArgsConstructor
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/placeOrder")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> placeOrder(@RequestBody OrderRequest orderRequest){
        return  ResponseEntity.ok().body(orderService.placeOrder(orderRequest));
    }
    @PutMapping("/updateOrder")
    public ResponseEntity<String> updateOrderPaymentStatus(@RequestParam String orderNumber, @RequestParam String paymentStatus){
        orderService.updatePaymentStatus(orderNumber,paymentStatus);
        return ResponseEntity.ok().body(String.format("Payment Status for order number: %s is updated successfully", orderNumber));
    }
}
