package com.ecommerce.Order.service;

import com.ecommerce.Order.dto.OrderListDTO;
import com.ecommerce.Order.dto.OrderRequest;
import com.ecommerce.Order.model.Order;
import com.ecommerce.Order.model.OrderListItems;
import com.ecommerce.Order.repository.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public String placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        List<OrderListItems> orderListItems = orderRequest.getOrderListDTOS().stream().map(
                this:: mapToDto
        ).toList();
        order.setOrderListItems(orderListItems);
        orderRepository.save(order);
        return"order placed";
    }

    private OrderListItems mapToDto(OrderListDTO orderListDTO) {
        OrderListItems orderListItems = new OrderListItems();
        orderListItems.setP_id(orderListDTO.getP_id());
        orderListItems.setPrice(orderListDTO.getPrice());
        orderListItems.setQuantity((orderListDTO.getQuantity()));
        return orderListItems;

    }
}
