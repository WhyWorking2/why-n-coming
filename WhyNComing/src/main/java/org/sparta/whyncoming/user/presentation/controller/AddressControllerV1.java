package org.sparta.whyncoming.user.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.sparta.whyncoming.common.exception.ErrorCode;
import org.sparta.whyncoming.common.response.ApiResult;
import org.sparta.whyncoming.common.response.ResponseUtil;
import org.sparta.whyncoming.common.security.service.CustomUserDetailsInfo;
import org.sparta.whyncoming.user.application.service.AddressServiceV1;
import org.sparta.whyncoming.user.presentation.dto.request.AddressCreateRequestDtoV1;
import org.sparta.whyncoming.user.presentation.dto.request.AddressUpdateRequestDtoV1;
import org.sparta.whyncoming.user.presentation.dto.response.AddressResponseDtoV1;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/address")
@Tag(name = "address", description = "address 데이터 API")
public class AddressControllerV1 {

    private final AddressServiceV1 addressServiceV1;
    
    public AddressControllerV1(AddressServiceV1 addressServiceV1) {
        this.addressServiceV1 = addressServiceV1;
    }

    @Operation(summary = "배송지 등록", security = @SecurityRequirement(name = "BearerAuth"))
    @PostMapping("/addresses")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResult<AddressResponseDtoV1>> createAddress(
            @AuthenticationPrincipal CustomUserDetailsInfo userDetailsInfo,
            @Valid @RequestBody AddressCreateRequestDtoV1 request,
            BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors()) {
            return ResponseUtil.failure(ErrorCode.VALIDATION_FAILED, "배송지 등록 요청이 유효하지 않습니다.");
        }
        // 서비스에 위임 (addressServiceV1에 addAddress(...)를 구현하세요)
        AddressResponseDtoV1 response = addressServiceV1.addAddress(userDetailsInfo, request);
        return ResponseUtil.success("배송지 등록 성공", response);
    }

    @Operation(summary = "배송지 목록 조회", security = @SecurityRequirement(name = "BearerAuth"))
    @GetMapping("/addresses")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResult<List<AddressResponseDtoV1>>> getMyAddresses(
            @AuthenticationPrincipal CustomUserDetailsInfo userDetailsInfo
    ) {

        List<AddressResponseDtoV1> response = addressServiceV1.getAddresses(userDetailsInfo); // addressServiceV1에 getAddresses 구현 필요
        return ResponseUtil.success("배송지 목록 조회 성공", response);
    }

    @Operation(summary = "배송지 수정", security = @SecurityRequirement(name = "BearerAuth"))
    @PatchMapping("/{addressId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResult<AddressResponseDtoV1>> updateAddress(
            @AuthenticationPrincipal CustomUserDetailsInfo userDetailsInfo,
            @PathVariable("addressId") UUID addressId,
            @Valid @RequestBody AddressUpdateRequestDtoV1 request,
            BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors()) {
            return ResponseUtil.failure(ErrorCode.VALIDATION_FAILED, "배송지 수정 요청이 유효하지 않습니다.");
        }
        AddressResponseDtoV1 response = addressServiceV1.updateAddress(userDetailsInfo, addressId, request);
        return ResponseUtil.success("배송지 수정 성공", response);
    }


    @Operation(summary = "배송지 삭제", security = @SecurityRequirement(name = "BearerAuth"))
    @DeleteMapping("/{addressId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResult<Void>> deleteAddress(
            @AuthenticationPrincipal CustomUserDetailsInfo userDetailsInfo,
            @PathVariable("addressId") UUID addressId
    ) {
        if (userDetailsInfo == null) {
            return ResponseUtil.failure(ErrorCode.UNAUTHORIZED, "로그인되어 있지 않습니다.");
        }

        addressServiceV1.deleteAddress(userDetailsInfo, addressId);
        return ResponseUtil.success("배송지 삭제 성공", null);
    }
}
