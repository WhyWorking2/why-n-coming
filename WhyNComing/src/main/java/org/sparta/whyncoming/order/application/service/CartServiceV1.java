package org.sparta.whyncoming.order.application.service;

import org.sparta.whyncoming.common.exception.BusinessException;
import org.sparta.whyncoming.common.exception.ErrorCode;
import org.sparta.whyncoming.order.domain.entity.Cart;
import org.sparta.whyncoming.order.domain.repository.CartRepository;
import org.sparta.whyncoming.order.presentation.dto.request.AddCartItemRequestV1;
import org.sparta.whyncoming.order.presentation.dto.request.UpdateCartItemRequestV1;
import org.sparta.whyncoming.order.presentation.dto.response.AddCartItemResponseV1;
import org.sparta.whyncoming.order.presentation.dto.response.GetCartItemResponseV1;
import org.sparta.whyncoming.product.domain.entity.Product;
import org.sparta.whyncoming.product.domain.repository.ProductRepository;
import org.sparta.whyncoming.store.domain.entity.Store;
import org.sparta.whyncoming.store.domain.repository.StoreRepository;
import org.sparta.whyncoming.user.domain.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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

        // 이미 있는 상품 -> 수량만 변경
        Optional<Cart> hasCartItem = cartRepository.findByUserAndProduct(user, product);

        if (hasCartItem.isPresent()){
            // 있으면 수량만 변경
            Cart cart = hasCartItem.get();
            cart.addQuantity(request.getQuantity());
            return new AddCartItemResponseV1(cart);
        }else{
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

    @Transactional
    public List<GetCartItemResponseV1> getMyCartItems(User user){
        List<Cart> cartItems = cartRepository.findAllByUser_UserNo(user.getUserNo());

        return cartItems.stream()
                .map(GetCartItemResponseV1::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public GetCartItemResponseV1 updateCartItem(User user, UUID cartId, UpdateCartItemRequestV1 request){
        Cart cartItem = cartRepository.findById(cartId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "해당 장바구니 아이템을 찾을 수 없습니다."));

        if (!cartItem.getUser().getUserNo().equals(user.getUserNo())){
            throw new BusinessException(ErrorCode.FORBIDDEN, "본인의 장바구니만 수정 가능");
        }

        cartItem.updateQuantity(request.getQuantity());

        return new GetCartItemResponseV1(cartItem);
    }

    @Transactional
    public UUID deleteCartItem(User user, UUID cartId) {
        Cart cartItem = cartRepository.findById(cartId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "해당 장바구니 아이템을 찾을 수 없습니다."));

        if (!cartItem.getUser().getUserNo().equals(user.getUserNo())) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "본인의 장바구니만 삭제 가능");
        }

        cartRepository.delete(cartItem);

        return cartItem.getCartId();
    }
}
