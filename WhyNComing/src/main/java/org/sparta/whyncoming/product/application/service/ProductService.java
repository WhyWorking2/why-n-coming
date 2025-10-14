package org.sparta.whyncoming.product.application.service;

import lombok.extern.slf4j.Slf4j;
import org.sparta.whyncoming.product.domain.entity.Category;
import org.sparta.whyncoming.product.domain.entity.CategoryProduct;
import org.sparta.whyncoming.product.domain.entity.Product;
import org.sparta.whyncoming.product.domain.repository.CategoryRepository;
import org.sparta.whyncoming.product.domain.repository.ProductRepository;
import org.sparta.whyncoming.product.presentaion.dto.request.ProductRequestDto;
import org.sparta.whyncoming.product.presentaion.dto.request.ProductUpdateRequestDto;
import org.sparta.whyncoming.product.presentaion.dto.response.ProductResponseDto;
import org.sparta.whyncoming.store.domain.entity.Store;
import org.sparta.whyncoming.store.domain.repository.StoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.sparta.whyncoming.common.response.ErrorCode.NOT_FOUND;

@Slf4j
@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final StoreRepository storeRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, StoreRepository storeRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.storeRepository = storeRepository;
    }

    /**
     * 상품 생성에 대한
     * @param requestDto
     * @return 상품ResponseDto 생성자
     */
    public ProductResponseDto creatProduct(ProductRequestDto requestDto) {

        Store store = storeRepository.findByStoreName(requestDto.getStoreName())
                .orElseThrow(() -> new IllegalArgumentException(requestDto.getStoreName() + " : " + NOT_FOUND));

        Product product = productRepository.save(new Product(store, requestDto.getProductName(), requestDto.getDescription(), requestDto.getPrice(), requestDto.getProductPictureUrl(), new ArrayList<>())); // 빈 카테고리로 생성

        List<CategoryProduct> categoryProducts = requestDto.getCategoryNames().stream()
                .map(name -> {
                    Category category = categoryRepository.findByCategoryName(name)
                            .orElseThrow(() -> new RuntimeException(name + " : " + NOT_FOUND));
                    return new CategoryProduct(product, category);
                })
                .toList();

        product.getCategoryProducts().addAll(categoryProducts);
        productRepository.save(product);
        return new ProductResponseDto(product);
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDto> readAllProducts() {

        return productRepository.findAll().stream()
                .map(product -> new ProductResponseDto(
                        product.getProductId(),
                        product.getStore().getStoreName(),
                        product.getProductName(),
                        product.getPrice(),
                        product.getProductPictureUrl(),
                        product.getCreatedDate(),
                        product.getModifiedDate()
                )).toList();
    }

    public ProductResponseDto updateProduct(UUID productId, ProductUpdateRequestDto requestDto) {
        Product product = productRepository.findByProductId(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품 없음 : " + requestDto.getProductName()));

        product.update(
                requestDto.getProductName(),
                requestDto.getPrice(),
                requestDto.getDescription(),
                requestDto.getProductPictureUrl()
        );

        Product updateProduct = productRepository.save(product);

        return new ProductResponseDto(updateProduct);
    }
}
