package com.ecommerce.Order.service;

import com.ecommerce.Order.dto.*;
import com.ecommerce.Order.model.Order;
import com.ecommerce.Order.model.OrderListItems;
import com.ecommerce.Order.repository.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private KafkaTemplate<String, OrderEvent> kafkaTemplate;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private Environment environment;

    @Override
    public String placeOrder(OrderRequest orderRequest) {
        Order order = createOrder(orderRequest);
        BigDecimal totalAmount = calculateTotalAmount(order.getOrderListItems());
        order.setTotalAmount(totalAmount);
        checkProductAvailability(order);
        orderRepository.save(order);
        sendPaymentEvent(order);

        return formatOrderConfirmation(order);
    }

    private Order createOrder(OrderRequest orderRequest) {
        log.info("Creating order...");
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderListItems> orderListItems = orderRequest.getOrderListDTOS().stream()
                .map(orderListDTO -> modelMapper.map(orderListDTO, OrderListItems.class))
                .toList();
        order.setOrderListItems(orderListItems);
        order.setPaymentStatus("Pending");
        log.info("order created successfully {}", order);
        return order;
    }

    private BigDecimal calculateTotalAmount(List<OrderListItems> orderListItems) {
        log.info("Calculating total amount");
        return orderListItems.stream()
                .map(OrderListItems::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void checkProductAvailability(Order order) {
        List<String> pIds = order.getOrderListItems().stream()
                .map(OrderListItems::getP_id)
                .toList();
        log.info("Checking product availability for {}", pIds);
        String inventoryBaseURL = environment.matchesProfiles("docker") ? "http://inventory:8084" : "http://localhost:8084";
        log.info("Active Profile is {} and base URL is: {}", environment.getActiveProfiles(), inventoryBaseURL);
        WebClient webClient = WebClient.create(inventoryBaseURL);
        OrderCheckStock[] orderCheckStocks = webClient
                .get()
                .uri(uriBuilder -> uriBuilder.path("api/product/inventory/check-stock")
                        .queryParam("p_id", pIds)
                        .build())
                .retrieve()
                .bodyToMono(OrderCheckStock[].class)
                .block();
        boolean allProductInStock = Arrays.stream(orderCheckStocks)
                .allMatch(OrderCheckStock::is_inStock);
        if (!allProductInStock) {
            log.error("Product is not in Stock, please try again later");
            throw new IllegalArgumentException("Product is not in Stock, please try again later");
        }

    }

    private void sendPaymentEvent(Order order) {
        List<String> pIds = order.getOrderListItems().stream()
                .map(OrderListItems::getP_id)
                .toList();
        OrderEvent orderEvent = new OrderEvent(
                order.getOrderNumber(),
                pIds,
                order.getPaymentStatus(),
                order.getTotalAmount()
        );
        kafkaTemplate.send("paymentTopic", orderEvent);
        log.info("Sent order further for payment processing, {}", orderEvent);
    }

    private String formatOrderConfirmation(Order order) {
        List<String> pIds = order.getOrderListItems().stream()
                .map(OrderListItems::getP_id)
                .toList();
        return String.format("Order Placed with -> {Order Id: %d, OrderNumber: %s, Payment Status: %s, Total Amount: %s, Products Ordered: %s}",
                order.getId(),
                order.getOrderNumber(),
                order.getPaymentStatus(),
                order.getTotalAmount(),
                pIds);
    }
    @KafkaListener(topics = "order-topic")
    public void updatePaymentStatus(HashMap<String, String> paymentResponse){
        log.info("Received an event from payment service ---> {}", paymentResponse);
        Order order = orderRepository.findByOrderNumber(paymentResponse.get("orderNumber"));
        if (order == null){
            log.error("No such order  with order number found {}", paymentResponse.get("orderNumber"));
            return;
        }
        log.info("Setting Payment status to {}" , paymentResponse.get("paymentStatus"));
        order.setPaymentStatus(paymentResponse.get("paymentStatus"));
        orderRepository.save(order);
        log.info("Payment set to {} for order number: {}",paymentResponse.get("paymentStatus"), paymentResponse.get("orderNumber"));
    }
}
