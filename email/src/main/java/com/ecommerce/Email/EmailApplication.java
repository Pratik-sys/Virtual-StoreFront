package com.ecommerce.Email;

import com.ecommerce.Email.dto.PaymentResponse;
import com.ecommerce.Email.dto.ProductFetch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.List;

@SpringBootApplication
@Slf4j
public class EmailApplication {

	@Autowired
	private Environment environment;

	public static void main(String[] args) {
		SpringApplication.run(EmailApplication.class, args);
	}


	@KafkaListener(topics = "emailTopic")
	public void	SendEmail(PaymentResponse paymentResponse){
		log.info("Received event from payment service {}", paymentResponse);
		log.info("Calling product service via webclient to fetch product details");
		log.info("Active profile is : {}", environment.getActiveProfiles());
		String productBaseURL = environment.matchesProfiles("docker") ? "http://product:8080" : "http://localhost:8080";
			WebClient webClient = WebClient.create(productBaseURL);
			List<String> getProductIds = paymentResponse.getP_id();
			for(String productId: getProductIds) {
				Mono<ProductFetch> result= fetchProductDetails(webClient, productId);
				log.info("Order Placed, Order number is: {} and list of products ordered: {}", paymentResponse.getOrderNumber(), result.block());
			}
		}
	private Mono<ProductFetch> fetchProductDetails(WebClient webClient, String productId) {
		return webClient.get()
				.uri("/api/product/{productId}", productId)
				.retrieve()
				.bodyToMono(ProductFetch.class);
	}
	}


