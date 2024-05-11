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
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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
        log.info("Active Profile is : {} ", environment.getActiveProfiles());
        if(environment.matchesProfiles("docker")){
            WebClient webClient = WebClient.create("http://inventory:8084");
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
        else{
            WebClient webClient = WebClient.create("http://localhost:8084");
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
    public void updatePaymentStatus(String orderNumber, String paymentStatus ){
        log.info("Finding order by order number {}",orderNumber );
        Order order = orderRepository.findByOrderNumber(orderNumber);
        if (order == null){
            log.error("No such order  with order number found {}", orderNumber);
            return;
        }
        log.info("Setting Payment status to {}",paymentStatus);
        order.setPaymentStatus(paymentStatus);
        orderRepository.save(order);
        log.info("Order update successfully {}", order);
    }
}
