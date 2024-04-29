package com.ecommerce.Payment.service;

import com.ecommerce.Payment.dto.ConfirmPaymentResponse;
import com.ecommerce.Payment.dto.OrderPaymentResponse;
import com.ecommerce.Payment.model.Payment;
import com.ecommerce.Payment.repository.PaymentRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
@Service
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService  {

    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    KafkaTemplate<String, ConfirmPaymentResponse> kafkaTemplate;
    @Override
    public void makePayment(String id, BigDecimal amount){
        Optional<Payment> payment  = paymentRepository.findById(id);
        if(payment.isPresent() && payment.get().getTotalAmount().equals(amount)){
            Payment pay = payment.get();
            pay.setPaymentStatus("Payment Confirmed");
            paymentRepository.save(pay);
            log.info("Received the payment for {} and payment status is updated to {}" , amount,pay.getPaymentStatus());
        }
        if (payment.isPresent() && payment.get().getPaymentStatus().equals("Payment Confirmed")){
            kafkaTemplate.send("emailTopic", new ConfirmPaymentResponse(
                    payment.get().getPaymentId(),
                    payment.get().getPaymentStatus(),
                    payment.get().getOrderNumber(),
                    payment.get().getTotalAmount(),
                    payment.get().getP_id()

            ));
        }
        else{
            log.error("Something went wrong do check");
        }
    }
}
