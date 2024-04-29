package com.ecommerce.Payment.controller;

import com.ecommerce.Payment.service.PaymentService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String makePayment(@PathVariable("id") String id, @RequestParam("amount")BigDecimal amount){
        paymentService.makePayment(id, amount);
        return "Payment Done";
    }

}
