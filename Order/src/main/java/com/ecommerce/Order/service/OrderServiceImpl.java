package com.ecommerce.Order.service;

import com.ecommerce.Order.dto.OrderCheckStock;
import com.ecommerce.Order.dto.OrderEvent;
import com.ecommerce.Order.dto.OrderListDTO;
import com.ecommerce.Order.dto.OrderRequest;
import com.ecommerce.Order.model.Order;
import com.ecommerce.Order.model.OrderListItems;
import com.ecommerce.Order.repository.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.math.BigDecimal;
import java.util.Arrays;
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

    @Override
    public String placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderListItems> orderListItems = orderRequest.getOrderListDTOS().stream().map(
                this:: mapToDto
        ).toList();
        // Calculate the total amount of the items that are ordered to send back to payment service
        BigDecimal totalAmount = orderListItems.stream().map(OrderListItems::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setOrderListItems(orderListItems);
        order.setPaymentStatus("Pending");
        order.setTotalAmount(totalAmount);

        // Make a web call to Inventory service for stock check for product_id
        List<String> p_id = orderListItems.stream().map(OrderListItems::getP_id).toList();
        log.info("Calling in Inventory service on localhost:8084");
        WebClient webClient = WebClient.create("http://localhost:8084");
        OrderCheckStock[] orderCheckStocks = webClient
                .get()
                .uri("api/product/inventory/check-stock",uriBuilder -> uriBuilder.queryParam("p_id", p_id).build())
                .retrieve()
                .bodyToMono(OrderCheckStock[].class).block();
        boolean allProductInStock = Arrays.stream(orderCheckStocks).allMatch(OrderCheckStock::is_inStock);
        log.info("Products availability {}", allProductInStock);
        if(allProductInStock){
            orderRepository.save(order);
            kafkaTemplate.send("paymentTopic", new OrderEvent(
                    order.getOrderNumber(),
                    p_id,
                    order.getPaymentStatus(),
                    order.getTotalAmount()));
            return String.format("Order Placed with -> {Order Id: %d, OrderNumber: %s, Payment Status: %s, Total Amount: %s, Products Ordered: %s}",
                    order.getId(),
                    order.getOrderNumber(),
                    order.getPaymentStatus(),
                    order.getTotalAmount(),
                    p_id
            );
        }
        else {
            throw  new IllegalArgumentException("Product is not in Stock, please try again later");
        }

    }

    private OrderListItems mapToDto(OrderListDTO orderListDTO) {
        return OrderListItems.builder()
                .id(orderListDTO.getId())
                .p_id(orderListDTO.getP_id())
                .price(orderListDTO.getPrice())
                .quantity(orderListDTO.getQuantity())
                .build();

    }
}
