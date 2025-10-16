package org.sparta.whyncoming.order.application.service;

import jakarta.transaction.Transactional;
import org.sparta.whyncoming.order.domain.entity.Cart;
import org.sparta.whyncoming.order.domain.entity.Delivery;
import org.sparta.whyncoming.order.domain.entity.Order;
import org.sparta.whyncoming.order.domain.enums.DeliveryStatus;
import org.sparta.whyncoming.order.domain.enums.Status;
import org.sparta.whyncoming.order.domain.repository.DeliveryRepository;
import org.sparta.whyncoming.order.domain.repository.OrderRepository;
import org.sparta.whyncoming.order.domain.repository.ReviewRepository;
import org.sparta.whyncoming.order.presentation.dto.request.CreateOrderRequestV1;
import org.sparta.whyncoming.order.presentation.dto.request.CreatePaymentRequestV1;
import org.sparta.whyncoming.order.presentation.dto.response.*;
import org.sparta.whyncoming.store.domain.entity.Store;
import org.sparta.whyncoming.store.domain.repository.StoreRepository;
import org.sparta.whyncoming.user.domain.entity.Address;
import org.sparta.whyncoming.user.domain.entity.User;
import org.sparta.whyncoming.user.domain.repository.AddressRepository;
import org.sparta.whyncoming.user.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderServiceV1 {

    private final OrderRepository orderRepository;
    private final DeliveryRepository deliveryRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final AddressRepository addressRepository;

    public OrderServiceV1(OrderRepository orderRepository, DeliveryRepository deliveryRepository,
                          UserRepository userRepository, StoreRepository storeRepository, AddressRepository addressRepository) {
        this.orderRepository = orderRepository;
        this.deliveryRepository = deliveryRepository;
        this.userRepository = userRepository;
        this.storeRepository = storeRepository;
        this.addressRepository = addressRepository;
    }

    // 주문 요청
    @Transactional
    public OrderStatusResponseV1 createOrder(CreateOrderRequestV1 req) {

        User user = userRepository.findByUserNo(req.getUserNo())
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));

        Store store = storeRepository.findByStoreId(req.getStoreId())
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

        User user = userRepository.findByUserNo(req.getUserNo())
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

    // 주문 리스트 조회
    public List<GetOrderListResponseV1> getOrderList(Integer userNo) {
        return orderRepository.findAllByUser_UserNo(userNo).stream()
                .map(order -> new GetOrderListResponseV1(order.getOrderId(), order.getStatus(), order.getStore().getStoreId()))
                .toList();
    }

    // 주문 상세 조회
    @Transactional
    public GetOrderDetailResponseV1 getOrderDetail(UUID orderId) {
        Order order = findOrderOrThrow(orderId);

        List<GetOrderDetailResponseV1.OrderItemResponseV1> items = order.getCarts().stream()
                .map(cart -> new GetOrderDetailResponseV1.OrderItemResponseV1(
                        cart.getProduct().getProductId(),
                        cart.getProduct().getProductName(),
                        cart.getQuantity()
                ))
                .toList();

        GetOrderDetailResponseV1.ReviewResponseV1 review = Optional.ofNullable(order.getReview())
                .map(r -> new GetOrderDetailResponseV1.ReviewResponseV1(
                        r.getReviewId(),
                        r.getReviewContent(),
                        r.getReviewRating(),
                        r.getReviewPictureUrl()
                ))
                .orElse(null);

        return new GetOrderDetailResponseV1(order.getOrderId(), items, review);
    }

    // 입점주 주문 리스트 조회
    public List<GetStoreOrderListResponseV1> getStoreOrderList(UUID storeId) {
        return orderRepository.findAllByStore_StoreId(storeId).stream()
                .map(order -> new GetStoreOrderListResponseV1(order.getOrderId(), order.getStatus(), order.getUser().getUserNo()))
                .toList();
    }

    // 입점주 주문 상세 조회
    @Transactional
    public GetStoreOrderDetailResponseV1 getStoreOrderDetail(UUID orderId) {
        Order order = findOrderOrThrow(orderId);

        List<GetStoreOrderDetailResponseV1.OrderItemResponseV1> items = order.getCarts().stream()
                .map(cart -> new GetStoreOrderDetailResponseV1.OrderItemResponseV1(
                        cart.getProduct().getProductId(),
                        cart.getProduct().getProductName(),
                        cart.getQuantity()
                ))
                .toList();

        GetStoreOrderDetailResponseV1.ReviewResponseV1 review = Optional.ofNullable(order.getReview())
                .map(r -> new GetStoreOrderDetailResponseV1.ReviewResponseV1(
                        r.getReviewId(),
                        r.getReviewContent(),
                        r.getReviewRating(),
                        r.getReviewPictureUrl()
                ))
                .orElse(null);

        return new GetStoreOrderDetailResponseV1(order.getOrderId(), items, review);
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
