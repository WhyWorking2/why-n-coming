package org.sparta.whyncoming.order.application.service;

import jakarta.transaction.Transactional;
import org.sparta.whyncoming.order.domain.entity.Delivery;
import org.sparta.whyncoming.order.domain.entity.Order;
import org.sparta.whyncoming.order.domain.enums.DeliveryStatus;
import org.sparta.whyncoming.order.domain.repository.DeliveryRepository;
import org.sparta.whyncoming.order.domain.repository.OrderRepository;
import org.sparta.whyncoming.order.presentation.dto.response.DeliveryStatusResponseV1;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class DeliveryServiceV1 {

    private final OrderRepository orderRepository;
    private final DeliveryRepository deliveryRepository;

    public DeliveryServiceV1(OrderRepository orderRepository, DeliveryRepository deliveryRepository) {
        this.orderRepository = orderRepository;
        this.deliveryRepository = deliveryRepository;
    }

    // 배달 조회
    public DeliveryStatusResponseV1 readDeliveryStatus(UUID orderId) {
        Delivery delivery = deliveryRepository.findByOrder_OrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Delivery not found for order: " + orderId));
        return new DeliveryStatusResponseV1(delivery.getDeliveryPosition(), delivery.getDeliveryStatus());
    }

    // 배달 수락
    @Transactional
    public DeliveryStatusResponseV1 acceptDelivery(UUID orderId) {
        Delivery delivery = findDeliveryOrThrow(orderId);
        delivery.updateDeliveryStatus(DeliveryStatus.ACCEPTED);
        return new DeliveryStatusResponseV1(delivery.getDeliveryStatus());
    }

    // 조리 완료
    @Transactional
    public DeliveryStatusResponseV1 cookedDelivery(UUID orderId) {
        Delivery delivery = findDeliveryOrThrow(orderId);
        delivery.updateDeliveryStatus(DeliveryStatus.COOKED);
        return new DeliveryStatusResponseV1(delivery.getDeliveryStatus());
    }

    // 배달 시작
    @Transactional
    public DeliveryStatusResponseV1 startDelivery(UUID orderId) {
        Delivery delivery = findDeliveryOrThrow(orderId);
        delivery.updateDeliveryStatus(DeliveryStatus.DELIVERING);
        return new DeliveryStatusResponseV1(delivery.getDeliveryStatus());
    }

    // 배달 완료
    @Transactional
    public DeliveryStatusResponseV1 completeDelivery(UUID orderId) {
        Delivery delivery = findDeliveryOrThrow(orderId);
        delivery.updateDeliveryStatus(DeliveryStatus.DELIVERED);
        return new DeliveryStatusResponseV1(delivery.getDeliveryStatus());
    }

    // 공통 조회 메서드
    private Order findOrderOrThrow(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));
    }

    private Delivery findDeliveryOrThrow(UUID orderId) {
        return Optional.of(findOrderOrThrow(orderId))
                .map(Order::getDelivery)
                .orElseThrow(() -> new IllegalStateException("해당 주문에 배달 정보가 없습니다: " + orderId));
    }
}
