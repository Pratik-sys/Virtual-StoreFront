package com.ecommerce.Email;

import com.ecommerce.Email.dto.PaymentResponse;
import com.ecommerce.Email.dto.ProductFetch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@SpringBootApplication
@Slf4j
public class EmailApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmailApplication.class, args);
	}


	@KafkaListener(topics = "emailTopic")
	public void	SendEmail(PaymentResponse paymentResponse){
		log.info("Received event from payment service {}", paymentResponse);
		log.info("Calling product service via webclient to fetch product details");
		WebClient webClient = WebClient.create("http://localhost:8080");
		List<String> getPIds = paymentResponse.getP_id();
		for(String response: getPIds) {
			Mono<ProductFetch> result = webClient.get()
					.uri(String.format("/api/product/%s", response))
					.retrieve()
					.bodyToMono(ProductFetch.class);
			log.info("Order Placed, Order number is: {} and list of products ordered: {}", paymentResponse.getOrderNumber(), result.block());
		}
		}
	}


