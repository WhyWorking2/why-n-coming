package org.sparta.whyncoming.order.application.service;

import jakarta.transaction.Transactional;
import org.apache.http.HttpHeaders;
import org.sparta.whyncoming.order.domain.entity.Delivery;
import org.sparta.whyncoming.order.domain.entity.Order;
import org.sparta.whyncoming.order.domain.enums.DeliveryStatus;
import org.sparta.whyncoming.order.domain.repository.DeliveryRepository;
import org.sparta.whyncoming.order.domain.repository.OrderRepository;
import org.sparta.whyncoming.order.presentation.dto.reqeust.TossConfirmRequestV1;
import org.sparta.whyncoming.order.presentation.dto.response.OrderStatusResponseV1;
import org.sparta.whyncoming.order.presentation.dto.response.TossConfirmResponseV1;
import org.sparta.whyncoming.store.domain.repository.StoreRepository;
import org.sparta.whyncoming.user.domain.entity.Address;
import org.sparta.whyncoming.user.domain.entity.User;
import org.sparta.whyncoming.user.domain.repository.AddressRepository;
import org.sparta.whyncoming.user.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

@Service
public class TossPaymentServiceV1 {

    @Value("${toss.secret-key}")
    private String secretKey;

    private final WebClient webClient;
    private final OrderRepository orderRepository;
    private final DeliveryRepository deliveryRepository;
    private final UserRepository userRepository;

    public TossPaymentServiceV1(WebClient.Builder webClientBuilder,
                                OrderRepository orderRepository, DeliveryRepository deliveryRepository,
                                UserRepository userRepository) {
        this.webClient = webClientBuilder
                .baseUrl("https://api.tosspayments.com")
                .build();
        this.orderRepository = orderRepository;
        this.deliveryRepository = deliveryRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public TossConfirmResponseV1 confirmPayment(TossConfirmRequestV1 req) {

        Order order = findOrderOrThrow(req.getOrderId());
        order.pay("CARD", req.getRequests());
        Integer totalPrice = order.getTotalPrice();
        req.updateTotalPrice(totalPrice);

        // Toss Payment 코드
        String authHeader = "Basic " + Base64.getEncoder()
                .encodeToString((secretKey + ":").getBytes(StandardCharsets.UTF_8));

        TossConfirmResponseV1 tossResponse =  webClient.post()
                .uri("/v1/order/payment/confirm")
                .header(HttpHeaders.AUTHORIZATION, authHeader)
                .bodyValue(req)
                .retrieve()
                .bodyToMono(TossConfirmResponseV1.class)
                .block(); // 테스트용 (실서버에서는 예외 처리)


        // 기존 Payment 코드
        User user = userRepository.findByUserNo(order.getUser().getUserNo())
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));

        Address address = order.getUser().getAddresses().stream().findFirst()
                .orElseThrow(() -> new IllegalArgumentException("주소가 존재하지 않습니다."));

        Delivery delivery = new Delivery(
                order,
                address,
                user,
                DeliveryStatus.ACCEPTED,
                req.getContactNumber()
        );

        deliveryRepository.save(delivery);
        order.assignDelivery(delivery);

        return new TossConfirmResponseV1();
    }

    private Order findOrderOrThrow(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));
    }
}