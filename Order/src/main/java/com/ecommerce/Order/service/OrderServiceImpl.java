package com.ecommerce.Order.service;

import com.ecommerce.Order.dto.OrderEvent;
import com.ecommerce.Order.dto.OrderListDTO;
import com.ecommerce.Order.dto.OrderRequest;
import com.ecommerce.Order.model.Order;
import com.ecommerce.Order.model.OrderListItems;
import com.ecommerce.Order.repository.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@NoArgsConstructor
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
        orderRepository.save(order);
        List<String> pIds = new ArrayList<>();
        for(OrderListItems items : orderListItems){
            pIds.add(items.getP_id());
        }
        kafkaTemplate.send("paymentTopic", new OrderEvent(
                order.getOrderNumber(),
                pIds,
                order.getPaymentStatus(),
                order.getTotalAmount()));
        return String.format("Order Placed with -> {Order Id: %d, OrderNumber: %s, Payment Status: %s, Total Amount: %s, Products Ordered: %s}",
                order.getId(),
                order.getOrderNumber(),
                order.getPaymentStatus(),
                order.getTotalAmount(),
                pIds
        );
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
