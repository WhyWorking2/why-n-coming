package org.sparta.whyncoming.product.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.sparta.whyncoming.common.response.ApiResult;
import org.sparta.whyncoming.product.application.service.ProductService;
import org.sparta.whyncoming.product.presentation.dto.request.ProductRequestDto;
import org.sparta.whyncoming.product.presentation.dto.request.ProductUpdateRequestDto;
import org.sparta.whyncoming.product.presentation.dto.response.ProductByCategoryResponseDto;
import org.sparta.whyncoming.product.presentation.dto.response.ProductDetailResponseDto;
import org.sparta.whyncoming.product.presentation.dto.response.ProductActiveResponseDto;
import org.sparta.whyncoming.product.presentation.dto.response.ProductResponseDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
     * 관리자일 때
     * @param requestDto 추가할 상품정보
     * @param imageFile 상품의 이미지
     * @return 추가된 상품정보
     */
    @PreAuthorize(" hasRole('ADMIN') or hasRole('MANAGER') or (hasRole('OWNER') and #requestDto.storeName == authentication.name)")
    @Operation(summary = "상품 추가")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResult<ProductActiveResponseDto>> createProduct(
            @RequestPart("requestDto") @Valid ProductRequestDto requestDto,
            @RequestPart("imageFile")  MultipartFile imageFile) {

        return ResponseEntity.ok(ApiResult.ofSuccess(productService.creatProduct(requestDto, imageFile)));
    }


    /**
     * 상품 전체 조회 - 삭제되지 않은 상품만
     * @return 전체 상품 리스트
     */
    @Operation(summary = "상품 전체조회")
    @GetMapping
    public ResponseEntity<ApiResult<List<ProductActiveResponseDto>>> findByActiveProductList() {
        return ResponseEntity.ok(ApiResult.ofSuccess(productService.readAllActiveProducts()));
    }

    /**
     * 삭제된 상품까지 포함된 전체조회
     * @return 삭제된 상품만
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @Operation(summary = "관리자, 매니저용 전체조회 - 삭제된 상품 포함된 리스트")
    @GetMapping("/deleted")
    public ResponseEntity<ApiResult<List<ProductResponseDto>>> findByProductList() {
        return ResponseEntity.ok(ApiResult.ofSuccess(productService.readAllProducts()));
    }

    /**
     * 카테고리별 상품조회, 삭제되지 않은 상품만
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
     * 상품 상세조회, 삭제되지 않은 상품만
     * @param uuid 조회할 상품의 UUID
     * @return 조회된 상품의 상세정보
     */
    @Operation(summary = "상품 상세조회")
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
    @PutMapping(value = "/{uuid}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResult<ProductActiveResponseDto>> updateProduct
    ( @PathVariable UUID uuid,
      @Parameter(description = "수정할 상품 정보") //스웨거용 parameter 어노테이션
      @RequestPart("updateRequestDto") @Valid ProductUpdateRequestDto updateRequestDto,
      @Parameter(description = "업로드할 이미지")
      @RequestPart("imageFile") MultipartFile imageFile) {

        return ResponseEntity.ok(ApiResult.ofSuccess(productService.updateProduct(uuid, updateRequestDto, imageFile)));
    }

    // 삭제
    @Operation(summary = "상품 삭제")
    @DeleteMapping("/{uuid}")
    public ResponseEntity<ApiResult<String>> deleteProduct(@PathVariable UUID uuid) {
        String deleteProductId = productService.deleteProduct(uuid);
        return ResponseEntity.ok(ApiResult.ofSuccess("삭제된 상품의 UUID :", deleteProductId));
    }

}
