package com.ecommerce.Payment;

import com.ecommerce.Payment.dto.OrderPaymentResponse;
import com.ecommerce.Payment.model.Payment;
import com.ecommerce.Payment.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.UUID;

@SpringBootApplication
@Slf4j
public class PaymentApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymentApplication.class, args);

	}
	@Autowired
	private PaymentRepository paymentRepository;

	@KafkaListener(topics = "paymentTopic")
	public void receiveOrder(OrderPaymentResponse orderPaymentResponse){
		log.info("Received and event from order service {}", orderPaymentResponse);
		Payment payment = Payment.builder()
				.paymentId(UUID.randomUUID().toString())
				.totalAmount(orderPaymentResponse.getTotalAmount())
				.paymentStatus(orderPaymentResponse.getPaymentStatus())
				.p_id(orderPaymentResponse.getP_id())
				.orderNumber(orderPaymentResponse.getOrderNumber())
				.build();
		log.info("Building payment instance and saving it to database {}", payment);
		paymentRepository.save(payment);
	}
}
