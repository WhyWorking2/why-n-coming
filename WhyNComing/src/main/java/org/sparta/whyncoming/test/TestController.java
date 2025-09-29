package org.sparta.whyncoming.test;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
    public String check() {
        return "ok";
    }

    @Operation(summary = "데이터 시드 (쿼리스트링)")
    @PostMapping("/seed")
    public String seed(
            @Parameter(description = "내용", example = "블라블라")
            @RequestParam String name
    ) {
        repo.save(Test.builder().name(name).build());
        return "seeded";
    }

    @Operation(summary = "전체 조회")
    @GetMapping
    public List<Test> list() {
        return repo.findAll();
    }
}
