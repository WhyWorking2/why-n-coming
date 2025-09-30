package org.sparta.whyncoming.test;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.sparta.whyncoming.common.response.ApiResult;
import org.sparta.whyncoming.common.response.ResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
@Tag(name = "Test", description = "테스트 데이터 API")
public class TestController {

    private final TestRepository repo;

    @Operation(summary = "헬스체크")
    @GetMapping("/db")
    public ResponseEntity<ApiResult<String>> check() {
        return ResponseUtil.success("ok");
    }

    @Operation(summary = "데이터 시드 (쿼리스트링)")
    @PostMapping("/seed")
    public ResponseEntity<ApiResult<Test>> seed(
            @Parameter(description = "내용", example = "블라블라")
            @RequestParam String name
    ) {
        Test saved = repo.save(Test.builder().name(name).build());
        return ResponseUtil.success("seeded", saved);
    }

    @Operation(summary = "전체 조회")
    @GetMapping
    public ResponseEntity<ApiResult<List<Test>>> list() {
        List<Test> all = repo.findAll();
        return ResponseUtil.success("조회 성공", all);
    }
}
