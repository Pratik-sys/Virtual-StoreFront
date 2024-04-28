package com.ecommerce.Email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
@Slf4j
public class EmailApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmailApplication.class, args);
	}
	@KafkaListener(topics = "emailTopic")
	public void	SendEmail(OrderResponse orderResponse){
		log.info("Order Placed, find the orderId and order number for future reference -> {}", orderResponse.toString());

	}

}
