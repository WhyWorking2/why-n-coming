package org.sparta.whyncoming.order.presentation.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.sparta.whyncoming.common.response.ApiResult;
import org.sparta.whyncoming.common.response.ResponseUtil;
import org.sparta.whyncoming.common.security.service.CustomUserDetailsInfo;
import org.sparta.whyncoming.order.application.service.CartServiceV1;
import org.sparta.whyncoming.order.presentation.dto.request.InsertCartItemRequestV1;
import org.sparta.whyncoming.order.presentation.dto.request.UpdateCartItemRequestV1;
import org.sparta.whyncoming.order.presentation.dto.response.InsertCartItemResponseV1;
import org.sparta.whyncoming.order.presentation.dto.response.DeleteCartItemResponseV1;
import org.sparta.whyncoming.order.presentation.dto.response.GetCartItemResponseV1;
import org.sparta.whyncoming.user.domain.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/carts")
@Tag(name = "Cart", description = "장바구니 API")
public class CartControllerV1 {

    private final CartServiceV1 cartService;

    public CartControllerV1(CartServiceV1 cartService){
        this.cartService = cartService;
    }

    @PostMapping("/products")
    public ResponseEntity<ApiResult<InsertCartItemResponseV1>> insertItemToCart(
            @AuthenticationPrincipal CustomUserDetailsInfo userDetailsInfo,
            @Valid @RequestBody InsertCartItemRequestV1 request
            ){
        User user = userDetailsInfo.getUser();

        InsertCartItemResponseV1 responseDto = cartService.insertItemToCart(user, request);

        return ResponseUtil.success(responseDto);
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResult<List<GetCartItemResponseV1>>> getMyCartItems(
            @AuthenticationPrincipal CustomUserDetailsInfo userDetailsInfo
    ){
        User user = userDetailsInfo.getUser();

        List<GetCartItemResponseV1> responseDtoList = cartService.getMyCartItems(user);

        return ResponseUtil.success(responseDtoList);
    }

    @PutMapping("/products/{cartId}")
    public ResponseEntity<ApiResult<GetCartItemResponseV1>> updateCartItem(
            @AuthenticationPrincipal CustomUserDetailsInfo userDetailsInfo,
            @PathVariable UUID cartId,
            @Valid @RequestBody UpdateCartItemRequestV1 request
    ){
        User user = userDetailsInfo.getUser();
        GetCartItemResponseV1 responseDto = cartService.updateCartItem(user, cartId, request);
        return ResponseUtil.success(responseDto);
    }

    @DeleteMapping("/products/{cartId}")
    public ResponseEntity<ApiResult<DeleteCartItemResponseV1>> deleteCartItem (
            @AuthenticationPrincipal CustomUserDetailsInfo userDetailsInfo,
            @PathVariable UUID cartId
    ){
        User user = userDetailsInfo.getUser();
        UUID deletedCartId = cartService.deleteCartItem(user, cartId);
        DeleteCartItemResponseV1 responseDto = new DeleteCartItemResponseV1(deletedCartId);

        return ResponseUtil.success(responseDto);
    }
}
