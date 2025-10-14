package org.sparta.whyncoming.product.presentaion.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sparta.whyncoming.common.response.ApiResult;
import org.sparta.whyncoming.product.application.service.ProductService;
import org.sparta.whyncoming.product.domain.entity.Product;
import org.sparta.whyncoming.product.presentaion.dto.request.ProductRequestDto;
import org.sparta.whyncoming.product.presentaion.dto.request.ProductUpdateRequestDto;
import org.sparta.whyncoming.product.presentaion.dto.response.ProductResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products")
@Tag(name = "Product", description = "상품 관련 API")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "상품 추가")
    @PostMapping
    public ResponseEntity<ApiResult<ProductResponseDto>> createProduct(@RequestBody ProductRequestDto requestDto) {
        return ResponseEntity.ok(ApiResult.ofSuccess(productService.creatProduct(requestDto)));
    }


    @Operation(summary = "상품 전체조회")
    @GetMapping("/list")
    public ResponseEntity<ApiResult<List<ProductResponseDto>>> findByProductList() {
        return ResponseEntity.ok(ApiResult.ofSuccess(productService.readAllProducts()));
    }
// -> 상세조회는 일단 패스
//    // 단일 조회
//    @GetMapping("/{id}")
//    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
//        return ResponseEntity.ok(productService.findById(id));
//    }
//

    /**
     * 상품 식별자까지 UUID라면 상품 개별 조회를 할 때 이름이 동일한 다른 가게 음식 등을 취급할 수 있어서 좋지만
     * 조금 길어서 개발 시에 불편감이 있을 수도 있을 거 같아요.
     * 일단은 UUID로 조회하는 것으로 하되, 다른 방식에 대해 생각해 볼 필요가 있어 보입니다.
     */
    @Operation(summary = "상품 수정")
    @PutMapping("/{uuid}")
    public ResponseEntity<ApiResult<ProductResponseDto>> updateProduct
    (@PathVariable UUID uuid, @RequestBody ProductUpdateRequestDto updateRequestDto) {

        return ResponseEntity.ok(ApiResult.ofSuccess(productService.updateProduct(uuid, updateRequestDto)));
    }

    // 삭제
    @DeleteMapping("/{uuid}")
    public ResponseEntity<ApiResult<String>> deleteProduct(@PathVariable UUID uuid) {
        String deleteProductId = productService.deleteProduct(uuid);
        return ResponseEntity.ok(ApiResult.ofSuccess("삭제된 상품의 UUID :", deleteProductId));
    }

}
