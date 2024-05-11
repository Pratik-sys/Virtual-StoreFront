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
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
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
        log.info("Updating payment status to 'Payment Confirmed' for id: {} in order service",payment.getOrderNumber());
        log.info("calling order service to update payment status");
        log.info("Active Profile is : {} ", environment.getActiveProfiles());
        if(environment.matchesProfiles("docker")){
            WebClient webClient = WebClient.create("http://order:8081");
            Mono<String> result = webClient
                    .put()
                    .uri(uriBuilder -> uriBuilder.path("api/product/order/updateOrder")
                            .queryParam("orderNumber", String.valueOf(payment.getOrderNumber()))
                            .queryParam("paymentStatus", String.valueOf(payment.getPaymentStatus())).build()
                    )
                    .retrieve()
                    .bodyToMono(String.class);
            log.info("sent request to order service with object {}", result.block());
        }
        else{
            WebClient webClient = WebClient.create("http://localhost:8081");
            Mono<String> result = webClient
                    .put()
                    .uri(uriBuilder -> uriBuilder.path("api/product/order/updateOrder")
                            .queryParam("orderNumber", String.valueOf(payment.getOrderNumber()))
                            .queryParam("paymentStatus", String.valueOf(payment.getPaymentStatus())).build()
                    )
                    .retrieve()
                    .bodyToMono(String.class);
            log.info("sent request to order service with object {}", result.block());
        }


    }

}
