package org.sparta.whyncoming.order.application.service;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpHeaders;
import org.sparta.whyncoming.order.domain.entity.Delivery;
import org.sparta.whyncoming.order.domain.entity.Order;
import org.sparta.whyncoming.order.domain.enums.DeliveryStatus;
import org.sparta.whyncoming.order.domain.repository.DeliveryRepository;
import org.sparta.whyncoming.order.domain.repository.OrderRepository;
import org.sparta.whyncoming.order.presentation.dto.request.TossConfirmRequestV1;
import org.sparta.whyncoming.order.presentation.dto.response.PaymentInfoResponseV1;
import org.sparta.whyncoming.order.presentation.dto.response.TossConfirmResponseV1;
import org.sparta.whyncoming.user.domain.entity.Address;
import org.sparta.whyncoming.user.domain.entity.User;
import org.sparta.whyncoming.user.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

@Service
public class TossPaymentServiceV1 {

    @Value("${toss.secret-key}")
    private String secretKey;

    @Value("${toss.client-key}")
    private String clientKey;

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
    public Map<String, Object> getClientKey(UUID orderId) {
        Order order = findOrderOrThrow(orderId);
        int amount = order.getTotalPrice();

        return Map.of(
                "clientKey", clientKey,
                "amount", amount
        );
    }

    @Transactional
    public TossConfirmResponseV1 confirmPayment(TossConfirmRequestV1 req) {

        UUID orderId = UUID.fromString(req.getOrderId());
        Order order = findOrderOrThrow(orderId);
        order.tossPay("CARD", "많이 주세요~");
        Integer totalPrice = order.getTotalPrice();

        // Toss Payment 코드
        String authHeader = "Basic " + Base64.getEncoder()
                .encodeToString((secretKey + ":").getBytes(StandardCharsets.UTF_8));

        TossConfirmResponseV1 tossResponse =  webClient.post()
                .uri("/v1/payments/confirm")
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
                address.getAddress()
        );

        deliveryRepository.save(delivery);
        order.assignDelivery(delivery);

        return new TossConfirmResponseV1();
    }

    private Order findOrderOrThrow(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));
    }

    @Transactional
    public PaymentInfoResponseV1 getPaymentInfo(String paymentKey, UUID orderId) {
        String authHeader = "Basic " + Base64.getEncoder()
                .encodeToString((secretKey + ":").getBytes(StandardCharsets.UTF_8));

        PaymentInfoResponseV1 paymentInfo = webClient.get()
                .uri("/v1/payments/{paymentKey}", paymentKey)
                .header(HttpHeaders.AUTHORIZATION, authHeader)
                .retrieve()
                .bodyToMono(PaymentInfoResponseV1.class)
                .block(); // 테스트용 동기 호출

        return paymentInfo;
    }
}