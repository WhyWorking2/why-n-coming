package org.sparta.whyncoming.order.application.service;

import jakarta.transaction.Transactional;
import org.sparta.whyncoming.order.domain.entity.Cart;
import org.sparta.whyncoming.order.domain.entity.Delivery;
import org.sparta.whyncoming.order.domain.entity.Order;
import org.sparta.whyncoming.order.domain.entity.Review;
import org.sparta.whyncoming.order.domain.enums.DeliveryStatus;
import org.sparta.whyncoming.order.domain.enums.Status;
import org.sparta.whyncoming.order.domain.repository.DeliveryRepository;
import org.sparta.whyncoming.order.domain.repository.OrderRepository;
import org.sparta.whyncoming.order.domain.repository.ReviewRepository;
import org.sparta.whyncoming.order.presentation.dto.reqeust.CreateOrderRequestV1;
import org.sparta.whyncoming.order.presentation.dto.reqeust.CreatePaymentRequestV1;
import org.sparta.whyncoming.order.presentation.dto.reqeust.CreateReviewRequestV1;
import org.sparta.whyncoming.order.presentation.dto.response.*;
import org.sparta.whyncoming.store.domain.entity.Store;
import org.sparta.whyncoming.store.domain.repository.StoreRepository;
import org.sparta.whyncoming.user.domain.entity.Address;
import org.sparta.whyncoming.user.domain.entity.User;
import org.sparta.whyncoming.user.domain.repository.AddressRepository;
import org.sparta.whyncoming.user.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceV1 {

    private final OrderRepository orderRepository;
    private final DeliveryRepository deliveryRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final AddressRepository addressRepository;

    public OrderServiceV1(OrderRepository orderRepository, DeliveryRepository deliveryRepository,
                          ReviewRepository reviewRepository, UserRepository userRepository,
                          StoreRepository storeRepository, AddressRepository addressRepository) {
        this.orderRepository = orderRepository;
        this.deliveryRepository = deliveryRepository;
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.storeRepository = storeRepository;
        this.addressRepository = addressRepository;
    }

    // 주문 요청
    @Transactional
    public OrderStatusResponseV1 createOrder(CreateOrderRequestV1 req) {

        User user = userRepository.findById(req.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));

        Store store = storeRepository.findById(req.getStoreId())
                .orElseThrow(() -> new IllegalArgumentException("입점주가 존재하지 않습니다."));

        int totalPrice = req.getItems().stream()
                .mapToInt(cart -> cart.getProduct().getPrice() * cart.getQuantity())
                .sum();

        List<Cart> carts = req.getItems();
        //carts.forEach(cart -> cart.setOrder(order)); // Order와 Cart 사이 mapping인데 당장 필요 없음

        Order order = new Order(
                store,
                user,
                null,
                totalPrice,
                null,
                Status.CREATED,
                null,
                carts,
                null
        );

        orderRepository.save(order);
        return new OrderStatusResponseV1(order.getStatus());
    }

    // 결제
    @Transactional
    public OrderStatusResponseV1 createPayment(UUID orderId, CreatePaymentRequestV1 req) {

        Order order = findOrderOrThrow(orderId);

        order.pay(req.getPaymentMethod(), req.getRequests());

        User user = userRepository.findById(req.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));

        Address address = addressRepository.findByUserAndAddress(user, req.getAddress())
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

        return new OrderStatusResponseV1(order.getStatus());
    }

    // 주문 취소
    @Transactional
    public OrderStatusResponseV1 cancelOrder(UUID orderId) {
        Order order = findOrderOrThrow(orderId);
        order.cancel(); // 엔티티 내부 상태 변경 메서드
        return new OrderStatusResponseV1(order.getStatus());
    }

    // 환불
    @Transactional
    public OrderStatusResponseV1 refundOrder(UUID orderId) {
        Order order = findOrderOrThrow(orderId);
        order.refund();
        return new OrderStatusResponseV1(order.getStatus());
    }

    // 배달 조회
    public DeliveryStatusResponseV1 readDeliveryStatus(UUID orderId) {
        Delivery delivery = deliveryRepository.findByOrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Delivery not found for order: " + orderId));
        return new DeliveryStatusResponseV1(delivery.getDeliveryPosition(), delivery.getDeliveryStatus());
    }

    // 주문 리스토 조회
    public List<GetOrderListResponseV1> getOrderList(UUID userId) {
        return orderRepository.findAllByUserId(userId).stream()
                .map(order -> new GetOrderListResponseV1(order.getId(), order.getStatus(), order.getStoreId()))
                .toList();
    }

    // 주문 상세 조회
    public GetOrderDetailResponseV1 getOrderDetail(UUID orderId) {
        Order order = findOrderOrThrow(orderId);
        return new GetOrderDetailResponseV1(order.getOrderId(), order.getStatus(), order.getItems(), order.getReview());
    }

    // 리뷰 작성
    @Transactional
    public OrderStatusResponseV1 writeReview(UUID orderId, CreateReviewRequestV1 req) {
        Order order = findOrderOrThrow(orderId);
        reviewRepository.save(new Review(orderId, req.getContent(), req.getRating(), req.getReviewPictureUrl()));
        return new OrderStatusResponseV1("SUCCESS");
    }

    // 배달 수락
    @Transactional
    public DeliveryStatusResponseV1 acceptDelivery(UUID orderId) {
        Order order = findOrderOrThrow(orderId);
        order.updateStatus("ACCEPTED");
        return new DeliveryStatusResponseV1("ACCEPTED");
    }

    // 조리 완료
    @Transactional
    public DeliveryStatusResponseV1 cookedDelivery(UUID orderId) {
        Order order = findOrderOrThrow(orderId);
        order.updateStatus("COOKED");
        return new DeliveryStatusResponseV1("COOKED");
    }

    // 배달 시작
    @Transactional
    public DeliveryStatusResponseV1 startDelivery(UUID orderId) {
        Order order = findOrderOrThrow(orderId);
        order.updateStatus("DELIVERING");
        return new DeliveryStatusResponseV1("DELIVERING");
    }

    // 배달 완료
    @Transactional
    public DeliveryStatusResponseV1 completeDelivery(UUID orderId) {
        Order order = findOrderOrThrow(orderId);
        order.updateStatus("DELIVERED");
        return new DeliveryStatusResponseV1("DELIVERED");
    }

    // 입점주 주문 리스트 조회
    public List<GetStoreOrderListResponseV1> getStoreOrderList(UUID storeId) {
        return orderRepository.findAllByStoreId(storeId).stream()
                .map(order -> new GetStoreOrderListResponseV1(order.getId(), order.getStatus(), order.getUserId()))
                .toList();
    }

    // 입점주 주문 상세 조회
    public GetStoreOrderDetailResponseV1 getStoreOrderDetail(UUID orderId) {
        Order order = findOrderOrThrow(orderId);
        return new GetStoreOrderDetailResponseV1(order.getOrderId(), order.getStatus(), order.getItems(), order.getReview());
    }

    // 공통 조회 메서드
    private Order findOrderOrThrow(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));
    }
}
