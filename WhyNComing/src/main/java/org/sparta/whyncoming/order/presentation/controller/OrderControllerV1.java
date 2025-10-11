package org.sparta.whyncoming.order.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sparta.whyncoming.common.response.ApiResult;
import org.sparta.whyncoming.common.response.ResponseUtil;
import org.sparta.whyncoming.order.application.service.OrderServiceV1;
import org.sparta.whyncoming.test.presentaion.dto.request.UpdateTestRequestV1;
import org.sparta.whyncoming.test.presentaion.dto.response.ReadTestResponseV1;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/order")
@Tag(name = "Order", description = "주문 데이터 API")
public class OrderControllerV1 {
    private final OrderServiceV1 service;

    public OrderControllerV1(OrderServiceV1 service) {
        this.service = service;
    }

    @Operation(summary = "주문 요청")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResult<ReadTestResponseV1>> updateTest(
            @PathVariable Long id,
            @RequestBody UpdateTestRequestV1 req
    ) {
        return ResponseUtil.success("수정 성공", service.updateTest(id, req));
    }
}
