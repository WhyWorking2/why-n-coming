package org.sparta.whyncoming.product.domain.controller;

import org.sparta.whyncoming.product.domain.entity.Product;
import org.sparta.whyncoming.product.domain.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
   // private final ProductService productService;

//    public ProductController(ProductService productService) {
//        this.productService = productService;
//    }
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
//    // 등록
//    @PostMapping
//    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
//        return ResponseEntity.ok(productService.save(product));
//    }
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
