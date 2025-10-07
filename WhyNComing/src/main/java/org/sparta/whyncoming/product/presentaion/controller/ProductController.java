package org.sparta.whyncoming.product.presentaion.controller;

import org.sparta.whyncoming.common.response.ApiResult;
import org.sparta.whyncoming.product.application.service.ProductService;
import org.sparta.whyncoming.product.presentaion.dto.request.ProductRequestDto;
import org.sparta.whyncoming.product.presentaion.dto.response.ProductResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // 생성
    @PostMapping
    public ResponseEntity<ApiResult<ProductResponseDto>> createProduct(@RequestBody ProductRequestDto requestDto) {
        return ResponseEntity.ok(ApiResult.ofSuccess(productService.creatProduct(requestDto)));
    }

//
//    // 전체 조회
//    @GetMapping
//    public ResponseEntity<List<Product>> getAllProducts() {
//        return ResponseEntity.ok(productService.findAll());
//    }
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
