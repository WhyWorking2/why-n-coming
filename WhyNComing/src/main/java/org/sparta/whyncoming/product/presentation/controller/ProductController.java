package org.sparta.whyncoming.product.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sparta.whyncoming.common.response.ApiResult;
import org.sparta.whyncoming.product.application.service.ProductService;
import org.sparta.whyncoming.product.presentation.dto.request.ProductRequestDto;
import org.sparta.whyncoming.product.presentation.dto.request.ProductUpdateRequestDto;
import org.sparta.whyncoming.product.presentation.dto.response.ProductByCategoryResponseDto;
import org.sparta.whyncoming.product.presentation.dto.response.ProductDetailResponseDto;
import org.sparta.whyncoming.product.presentation.dto.response.ProductResponseDto;
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

    /**
     * 상품 추가 컨트롤러
     * @param requestDto 입력된 상품의 정보
     * @return 생성된 상품의 정보
     */
    @Operation(summary = "상품 추가")
    @PostMapping
    public ResponseEntity<ApiResult<ProductResponseDto>> createProduct(
            @RequestBody ProductRequestDto requestDto) {
        return ResponseEntity.ok(ApiResult.ofSuccess(productService.creatProduct(requestDto)));
    }


    @Operation(summary = "상품 전체조회")
    @GetMapping
    public ResponseEntity<ApiResult<List<ProductResponseDto>>> findByProductList() {
        return ResponseEntity.ok(ApiResult.ofSuccess(productService.readAllProducts()));
    }

    /**
     * 카테고리별 상품조회
     *
     * @return 카테고리별로 조회된 상품 목록
     */
    @Operation(summary = "카테고리별 상품 조회")
    @GetMapping("/category")
    public ResponseEntity<ApiResult<List<ProductByCategoryResponseDto>>> findByCategory(
            @RequestParam String catetegoryName
    ) {
        return ResponseEntity.ok(ApiResult.ofSuccess(productService.readProductsByCategory(catetegoryName)));
    }


    /**
     * 상품 상세조회 (손님만)
     * @param uuid 조회할 상품의 UUID
     * @return 조회된 상품의 상세정보
     */
    @Operation(summary = "상품 상세조회(손님용)")
    @GetMapping("/{uuid}")
    public ResponseEntity<ApiResult<ProductDetailResponseDto>> getProductById(@PathVariable UUID uuid) {
        return ResponseEntity.ok(ApiResult.ofSuccess(productService.getProductDetail(uuid)));
    }


    /**
     * 상품 수정
     * @param uuid 수정할 상품의 UUID
     * @param updateRequestDto 수정할 상품 정보
     * @return 수정된 상품정보
     */
    @Operation(summary = "상품 수정")
    @PutMapping("/{uuid}")
    public ResponseEntity<ApiResult<ProductResponseDto>> updateProduct
    (@PathVariable UUID uuid, @RequestBody ProductUpdateRequestDto updateRequestDto) {

        return ResponseEntity.ok(ApiResult.ofSuccess(productService.updateProduct(uuid, updateRequestDto)));
    }

    // 삭제
    @Operation(summary = "상품 삭제")
    @DeleteMapping("/{uuid}")
    public ResponseEntity<ApiResult<String>> deleteProduct(@PathVariable UUID uuid) {
        String deleteProductId = productService.deleteProduct(uuid);
        return ResponseEntity.ok(ApiResult.ofSuccess("삭제된 상품의 UUID :", deleteProductId));
    }

}
