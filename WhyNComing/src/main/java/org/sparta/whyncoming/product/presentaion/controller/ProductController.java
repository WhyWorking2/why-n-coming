package org.sparta.whyncoming.product.presentaion.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sparta.whyncoming.common.response.ApiResult;
import org.sparta.whyncoming.product.application.service.ProductService;
import org.sparta.whyncoming.product.presentaion.dto.request.ProductRequestDto;
import org.sparta.whyncoming.product.presentaion.dto.response.ProductResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
//
//    // 단일 조회
//    @GetMapping("/{id}")
//    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
//        return ResponseEntity.ok(productService.findById(id));
//    }
//
//
//    // 수정
//    @PutMapping("/{id}")
//    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
//        return ResponseEntity.ok(productService.update(id, product));
//    }
//
//    // 삭제
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
//        productService.delete(id);
//        return ResponseEntity.noContent().build();
//    }

}
