package org.sparta.whyncoming.order.presentation.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.sparta.whyncoming.common.response.ApiResult;
import org.sparta.whyncoming.common.response.ResponseUtil;
import org.sparta.whyncoming.common.security.UserDetailsImpl;
import org.sparta.whyncoming.order.application.service.CartServiceV1;
import org.sparta.whyncoming.order.presentation.dto.request.AddCartItemRequestV1;
import org.sparta.whyncoming.order.presentation.dto.response.AddCartItemResponseV1;
import org.sparta.whyncoming.order.presentation.dto.response.GetCartItemResponseV1;
import org.sparta.whyncoming.user.domain.entity.User;
import org.sparta.whyncoming.user.domain.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/carts")
@Tag(name = "Cart", description = "장바구니 API")
public class CartControllerV1 {

    private final CartServiceV1 cartService;

    public CartControllerV1(CartServiceV1 cartService){
        this.cartService = cartService;
    }

    @PostMapping("/products")
    public ResponseEntity<ApiResult<AddCartItemResponseV1>> insertItemToCart(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody AddCartItemRequestV1 request
            ){
        User user = userDetails.getUser();

        AddCartItemResponseV1 responseDto = cartService.insertItemToCart(user, request);

        return ResponseUtil.success(responseDto);
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResult<List<GetCartItemResponseV1>>> getMyCartItems(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        User user = userDetails.getUser();

        List<GetCartItemResponseV1> responseDtoList = cartService.getMyCartItems(user);

        return ResponseUtil.success(responseDtoList);
    }
}
