package com.ecommerce.Email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@SpringBootApplication
@Slf4j
public class EmailApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmailApplication.class, args);
		WebClient webClient = WebClient.create("http://localhost:8080");
		Mono<String> result = webClient.get()
				.uri("api/product/662a98a45bdaae1d66699ce6")
				.retrieve()
				.bodyToMono(String.class);
		System.out.println(result.block());
	}
//	@KafkaListener(topics = "emailTopic")
//	public void	SendEmail(OrderResponse orderResponse){
//		log.info("Order Placed, find the orderId and order number for future reference -> {}", orderResponse.toString());
//
//	}
	public  void syncCommunicationWithProductService(){
		WebClient webClient = WebClient.create("localhost:8080/api/product");
		Mono<String> result = webClient.get()
				.uri("/662a98a45bdaae1d66699ce6")
				.retrieve()
				.bodyToMono(String.class);
		result.subscribe(response ->{
			System.out.println(response);
		});

	}

}
