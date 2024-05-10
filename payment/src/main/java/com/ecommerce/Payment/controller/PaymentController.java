package com.ecommerce.Payment.controller;

import com.ecommerce.Payment.service.PaymentService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/product/payment")
@AllArgsConstructor
@NoArgsConstructor
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/make-payment/{id}")
    public ResponseEntity<String> makePayment(@PathVariable("id") String id, @RequestParam("amount") BigDecimal amount) {
        boolean paymentSuccessful = paymentService.makePayment(id, amount);
        if (paymentSuccessful) {
            return ResponseEntity.ok().body("Payment Successful");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment Failed: Invalid Payment details");
        }
    }
}
