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
import java.security.PublicKey;
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

//    @Override
//    public void makePayment(String id, BigDecimal amount){
//        log.info("Finding payment by id : {}", id);
//        Optional<Payment> payment  = paymentRepository.findById(id);
//        log.info("found Payment with amount of {}", amount);
//        if(payment.isPresent() && payment.get().getTotalAmount().equals(amount)){
//            log.info("Checking if payment is present and amount matches to existing one");
//            Payment pay = payment.get();
//            pay.setPaymentStatus("Payment Confirmed");
//            log.info("Set payment status to confirm");
//            paymentRepository.save(pay);
//            log.info("Received the payment for {} and payment status is updated to {}" , amount,pay.getPaymentStatus());
//        }
//        if (payment.isPresent() && payment.get().getPaymentStatus().equals("Payment Confirmed")){
//            log.info("checking if payment status of {} is set to 'Payment Confirmed' ", id);
//            kafkaTemplate.send("emailTopic", new ConfirmPaymentResponse(
//                    payment.get().getPaymentId(),
//                    payment.get().getPaymentStatus(),
//                    payment.get().getOrderNumber(),
//                    payment.get().getTotalAmount(),
//                    payment.get().getP_id()
//
//            ));
//        }
//        else{
//            log.error("Something went wrong do check");
//        }
//    }
    @Override
    public boolean makePayment(String id, BigDecimal amount){
        log.info("Finding payment by id: {}", id);
        Optional<Payment> optionalPayment = paymentRepository.findById(id);
        if(optionalPayment.isEmpty()){
            log.error("Payment with id: {} not found", id);
            return false;
        }
        Payment payment = optionalPayment.get();
        if (!payment.getTotalAmount().equals(amount)){
            log.error("Amount mismatch for payment with id {}: expected={}, actual={}", id, payment.getTotalAmount(), amount);
            return false;
        }
        updatePaymentStatus(payment);
        sendConfirmationEmail(payment);
        return true;
    }

    private void sendConfirmationEmail(Payment payment) {
        if (!"Payment Confirmed".equals(payment.getPaymentStatus())) {
            log.error("Payment status is not set to 'Payment Confirmed' for id: {}", payment.getPaymentId());
            return;
        }

        log.info("Sending confirmation email for payment with id: {}", payment.getPaymentId());
        ConfirmPaymentResponse confirmPaymentResponse = new ConfirmPaymentResponse(
                payment.getPaymentId(),
                payment.getPaymentStatus(),
                payment.getOrderNumber(),
                payment.getTotalAmount(),
                payment.getP_id()
        );
        kafkaTemplate.send("emailTopic", confirmPaymentResponse);
        log.info("Confirmation email sent successfully");
    }

    private void updatePaymentStatus(Payment payment) {
        log.info("Updating payment status to 'Payment Confirmed' for id: {}", payment.getPaymentId());
        payment.setPaymentStatus("Payment Confirmed");
        paymentRepository.save(payment);
        log.info("Payment status updated successfully");
    }

}
