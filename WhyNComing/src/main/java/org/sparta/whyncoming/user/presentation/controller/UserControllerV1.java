package org.sparta.whyncoming.user.presentation.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.sparta.whyncoming.common.exception.BusinessException;
import org.sparta.whyncoming.common.exception.ErrorCode;
import org.sparta.whyncoming.common.response.ApiResult;
import org.sparta.whyncoming.common.response.ResponseUtil;
import org.sparta.whyncoming.common.security.service.CustomUserDetailsInfo;
import org.sparta.whyncoming.user.application.service.UserServiceV1;

import org.sparta.whyncoming.user.presentation.dto.request.SignUpUserRequestDtoV1;


import org.sparta.whyncoming.user.presentation.dto.request.UpdateUserRequestDtoV1;
import org.sparta.whyncoming.user.presentation.dto.response.SingUpUserResponseDtoV1;
import org.sparta.whyncoming.user.presentation.dto.response.UpdateUserResponseDtoV1;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/user")
@Tag(name = "User", description = "User 데이터 API")
public class UserControllerV1 {

    private final UserServiceV1 userServiceV1;
    private final PasswordEncoder passwordEncoder;

    public UserControllerV1(UserServiceV1 userServiceV1, PasswordEncoder passwordEncoder) {
        this.userServiceV1 = userServiceV1;
        this.passwordEncoder = passwordEncoder;
    }

    @Operation(summary = "User 회원 가입")
    @PostMapping("/signup")
    public ResponseEntity<ApiResult<SingUpUserResponseDtoV1>> signup(@Valid SignUpUserRequestDtoV1 requestDto, BindingResult bindingResult) {
        // Validation 예외처리
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if(fieldErrors.size() > 0) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {

            }
            return ResponseUtil.failure(ErrorCode.VALIDATION_FAILED, ErrorCode.VALIDATION_FAILED.getMessage() + "회원 생성 실패");
        }

        return ResponseUtil.success("회원 생성 성공", userServiceV1.signup(requestDto));
    }

    @Operation(summary = "User 헬스체크")
    @GetMapping("/db")
    public ResponseEntity<ApiResult<String>> check() {
        return ResponseUtil.success(userServiceV1.health());
    }

    @Operation(summary = "User 업데이트")
    @PatchMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResult<UpdateUserResponseDtoV1>> updateMe(
            @AuthenticationPrincipal CustomUserDetailsInfo userDetailsInfo,
            @Valid @RequestBody UpdateUserRequestDtoV1 request,
            BindingResult bindingResult
    ) {
        if(userDetailsInfo == null){
            return ResponseUtil.failure(ErrorCode.VALIDATION_FAILED, "로그인되어 있지 않습니다.");
        }
        if (bindingResult.hasErrors()) {
            return ResponseUtil.failure(ErrorCode.VALIDATION_FAILED, "회원 수정 실패");
        }
        if (!passwordEncoder.matches(request.getOldPassword(), userDetailsInfo.getPassword())) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED,"현재 비밀번호를 확인해주세요.");
        }
        // TODO: 서비스 호출 후 결과 리턴
        UpdateUserResponseDtoV1 response = userServiceV1.updateSelf(userDetailsInfo, request);
        return ResponseUtil.success("회원 수정 성공", response);
    }
}
