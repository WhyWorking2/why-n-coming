package org.sparta.whyncoming.user.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import org.sparta.whyncoming.common.exception.BusinessException;
import org.sparta.whyncoming.common.exception.ErrorCode;
import org.sparta.whyncoming.common.response.ApiResult;
import org.sparta.whyncoming.common.response.ResponseUtil;
import org.sparta.whyncoming.common.security.service.CustomUserDetailsInfo;

import org.sparta.whyncoming.user.application.service.BosUserServiceV1;
import org.sparta.whyncoming.user.domain.enums.UserRoleEnum;
import org.sparta.whyncoming.user.presentation.dto.request.UpdateUserByAdminRequestDtoV1;
import org.sparta.whyncoming.user.presentation.dto.request.UpdateUserRequestDtoV1;
import org.sparta.whyncoming.user.presentation.dto.response.UpdateUserResponseDtoV1;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.security.core.annotation.AuthenticationPrincipal;


import io.swagger.v3.oas.annotations.tags.Tag;
import org.sparta.whyncoming.user.application.service.UserServiceV1;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/bos/user")
@Tag(name = "BosUser", description = "bos용 User 데이터 API")
@PreAuthorize("hasAnyRole('MANAGER','MASTER')")
public class BosUserControllerV1 {

    private final BosUserServiceV1 bosUserServiceV1;
    private final PasswordEncoder passwordEncoder;

    public BosUserControllerV1(BosUserServiceV1 bosUserServiceV1, PasswordEncoder passwordEncoder) {
        this.bosUserServiceV1 = bosUserServiceV1;
        this.passwordEncoder = passwordEncoder;
    }

    @Operation(summary = "User 업데이트", security = @SecurityRequirement(name = "BearerAuth"))
    @PatchMapping("/{userNo}")
    public ResponseEntity<ApiResult<UpdateUserResponseDtoV1>> updateMe(
            @PathVariable("userNo") Integer userNo,
            @AuthenticationPrincipal CustomUserDetailsInfo userDetailsInfo,
            @Valid @RequestBody UpdateUserByAdminRequestDtoV1 request,
            BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors()) {
            return ResponseUtil.failure(ErrorCode.VALIDATION_FAILED, "회원 수정 실패");
        }
        if(userDetailsInfo.getRole() == UserRoleEnum.MANAGER && request.getRole() == UserRoleEnum.MASTER) {
            return ResponseUtil.failure(ErrorCode.UNAUTHORIZED, "매니저 권한 초과 실패");
        }

        UpdateUserResponseDtoV1 response = bosUserServiceV1.updateUserInfo(userNo, request);
        return ResponseUtil.success("회원 수정 성공", response);
    }
}
