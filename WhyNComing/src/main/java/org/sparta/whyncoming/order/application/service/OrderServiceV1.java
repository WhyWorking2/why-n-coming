package org.sparta.whyncoming.order.application.service;

import org.sparta.whyncoming.order.domain.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceV1 {
    private final OrderRepository orderRepository;

    public OrderServiceV1(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
}
