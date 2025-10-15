package org.sparta.whyncoming.user.presentation.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.sparta.whyncoming.common.exception.ErrorCode;
import org.sparta.whyncoming.common.response.ApiResult;
import org.sparta.whyncoming.common.response.ResponseUtil;
import org.sparta.whyncoming.user.application.service.UserServiceV1;

import org.sparta.whyncoming.user.presentation.dto.request.SignUpUserRequestDtoV1;


import org.sparta.whyncoming.user.presentation.dto.response.SingUpUserResponseV1;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/user")
@Tag(name = "User", description = "User 데이터 API")
public class UserControllerV1 {

    private final UserServiceV1 userServiceV1;

    public UserControllerV1(UserServiceV1 userServiceV1) {
        this.userServiceV1 = userServiceV1;
    }

    @Operation(summary = "User 헬스체크")
    @PostMapping("/signup")
    public ResponseEntity<ApiResult<SingUpUserResponseV1>> signup(@Valid SignUpUserRequestDtoV1 requestDto, BindingResult bindingResult) {
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


/*


    @Operation(summary = "전체 조회")
    @GetMapping
    public ResponseEntity<ApiResult<List<ReadUserResponseV1>>> readAllTest() {
        return ResponseUtil.success("조회 성공", service.findAllTest());
    }

    @Operation(summary = "상세 조회")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<ReadUserResponseV1>> readTest(
            @Parameter(description = "ID", example = "1")
            @PathVariable Long id
    ) {
        return ResponseUtil.success("상세 조회 성공", service.findByIdTest(id));
    }

    @Operation(summary = "데이터 수정")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResult<ReadUserResponseV1>> updateTest(
            @PathVariable Long id,
            @RequestBody UpdateUserRequestV1 req
    ) {
        return ResponseUtil.success("수정 성공", service.updateTest(id, req));
    }

    @Operation(summary = "데이터 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResult<Void>> deleteTest(
            @PathVariable Long id
    ) {
        service.deleteTest(id);
        return ResponseUtil.success("삭제 성공", null);
    }

    @Operation(summary = "이름 검색")
    @GetMapping("/search")
    public ResponseEntity<ApiResult<List<ReadUserResponseV1>>> searchByName(
            @RequestParam String keyword
    ) {
        return ResponseUtil.success("검색 성공", service.searchByName(keyword));
    }*/
}
