package com.ecommerce.Payment.service;

import com.ecommerce.Payment.dto.ConfirmPaymentResponse;
import com.ecommerce.Payment.model.Payment;
import com.ecommerce.Payment.repository.PaymentRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.HashMap;
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

    @Autowired
    KafkaTemplate<String, HashMap<String, String>> template;

    @Autowired
    private Environment environment;

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
        updatePaymentStatusInOrders(payment);
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

    private void updatePaymentStatusInOrders(Payment payment){
        log.info("Sending event to order service, via 'order-topic' update payment for orderNumber: {}",payment.getOrderNumber());
        HashMap<String, String> orderDetails = new HashMap<>();
        log.info("Collecting needful parameters in HashMap");
        orderDetails.put("orderNumber", String.valueOf(payment.getOrderNumber()));
        orderDetails.put("paymentStatus", String.valueOf(payment.getPaymentStatus()));
        template.send("order-topic", orderDetails);
        log.info("Trigger sent to order service");
    }

}
