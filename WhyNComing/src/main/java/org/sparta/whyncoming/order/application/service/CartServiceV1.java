package org.sparta.whyncoming.order.application.service;

import org.sparta.whyncoming.common.exception.BusinessException;
import org.sparta.whyncoming.common.exception.ErrorCode;
import org.sparta.whyncoming.order.domain.entity.Cart;
import org.sparta.whyncoming.order.domain.repository.CartRepository;
import org.sparta.whyncoming.order.presentation.dto.request.AddCartItemRequestV1;
import org.sparta.whyncoming.order.presentation.dto.response.AddCartItemResponseV1;
import org.sparta.whyncoming.product.domain.entity.Product;
import org.sparta.whyncoming.product.domain.repository.ProductRepository;
import org.sparta.whyncoming.store.domain.entity.Store;
import org.sparta.whyncoming.store.domain.repository.StoreRepository;
import org.sparta.whyncoming.user.domain.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartServiceV1 {

    private final CartRepository cartRepository;
    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;

    public CartServiceV1(CartRepository cartRepository, StoreRepository storeRepository, ProductRepository productRepository){
        this.cartRepository = cartRepository;
        this.storeRepository = storeRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public AddCartItemResponseV1 insertItemToCart(User user, AddCartItemRequestV1 request){
        Store store = storeRepository.findByStoreId(request.getStoreId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "가게를 찾을 수 없습니다."));

        Product product = productRepository.findByProductId(request.getProductId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "상품을 찾을 수 없습니다."));

        // TODO: 이미 있는 상품 -> 수량만 변경

        Cart newCartItem = new Cart(
                store,
                product,
                null,
                user,
                request.getQuantity()
        );

        Cart savedCart = cartRepository.save(newCartItem);

        return new AddCartItemResponseV1(savedCart);
    }
}
