package com.ecommerce.Email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@Slf4j
public class EmailApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmailApplication.class, args);
	}
	@KafkaListener(topics = "emailTopic")
	public void	SendEmail(OrderResponse orderResponse){
		WebClient webClient = WebClient.create("http://localhost:8080");
		List<String> getPIds = orderResponse.getP_id();
		for(String response: getPIds) {
			Mono<ProductFetch> result = webClient.get()
					.uri(String.format("/api/product/%s", response))
					.retrieve()
					.bodyToMono(ProductFetch.class);
			log.info("Order Placed, Order number is: {} and list of products ordered: {}", orderResponse.getOrderNumber(), result.block());
		}
		}
	}


