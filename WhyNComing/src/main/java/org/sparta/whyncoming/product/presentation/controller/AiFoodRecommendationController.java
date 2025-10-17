package org.sparta.whyncoming.product.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sparta.whyncoming.common.response.ApiResult;
import org.sparta.whyncoming.product.application.service.AiFoodRecommendationService;
import org.sparta.whyncoming.product.presentation.dto.response.ProductAiResponseDto;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/products/ai")
@Tag(name = "Product", description = "상품 관련 API")
public class AiFoodRecommendationController {

    AiFoodRecommendationService aiFoodRecommendationService;

    public AiFoodRecommendationController(AiFoodRecommendationService aiFoodRecommendationService) {
        this.aiFoodRecommendationService = aiFoodRecommendationService;
    }

    @Operation(summary = "AI를 활용한 배달 음식 추천")
    @PostMapping("/recommend")
    public ResponseEntity<ApiResult<ProductAiResponseDto>> recommendFood(@RequestParam String foodPreference) {
        return ResponseEntity.ok(ApiResult.ofSuccess(aiFoodRecommendationService.recommendFood(foodPreference)));
    }
}
