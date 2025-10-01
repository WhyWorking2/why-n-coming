package org.sparta.whyncoming.test.presentaion.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.sparta.whyncoming.common.response.ApiResult;
import org.sparta.whyncoming.common.response.ResponseUtil;
import org.sparta.whyncoming.test.application.service.TestServiceV1;
import org.sparta.whyncoming.test.presentaion.dto.request.CreateTestRequestV1;
import org.sparta.whyncoming.test.presentaion.dto.request.UpdateTestRequestV1;
import org.sparta.whyncoming.test.presentaion.dto.response.ReadTestResponseV1;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/test")
@Tag(name = "Test", description = "테스트 데이터 API")
public class TestControllerV1 {

    private final TestServiceV1 service;

    public TestControllerV1(TestServiceV1 service) {
        this.service = service;
    }

    @Operation(summary = "헬스체크")
    @GetMapping("/db")
    public ResponseEntity<ApiResult<String>> check() {
        return ResponseUtil.success(service.health());
    }

    @Operation(summary = "데이터 생성")
    @PostMapping
    public ResponseEntity<ApiResult<ReadTestResponseV1>> createTest(
            @RequestBody CreateTestRequestV1 req
    ) {
        return ResponseUtil.success("생성 성공", service.createTest(req));
    }

    @Operation(summary = "전체 조회")
    @GetMapping
    public ResponseEntity<ApiResult<List<ReadTestResponseV1>>> readAllTest() {
        return ResponseUtil.success("조회 성공", service.findAllTest());
    }

    @Operation(summary = "상세 조회")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<ReadTestResponseV1>> readTest(
            @Parameter(description = "ID", example = "1")
            @PathVariable Long id
    ) {
        return ResponseUtil.success("상세 조회 성공", service.findByIdTest(id));
    }

    @Operation(summary = "데이터 수정")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResult<ReadTestResponseV1>> updateTest(
            @PathVariable Long id,
            @RequestBody UpdateTestRequestV1 req
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
    public ResponseEntity<ApiResult<List<ReadTestResponseV1>>> searchByName(
            @RequestParam String keyword
    ) {
        return ResponseUtil.success("검색 성공", service.searchByName(keyword));
    }
}