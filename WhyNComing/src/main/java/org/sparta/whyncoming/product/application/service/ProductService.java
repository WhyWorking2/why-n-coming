package org.sparta.whyncoming.product.application.service;

import lombok.extern.slf4j.Slf4j;
import org.sparta.whyncoming.product.domain.entity.Category;
import org.sparta.whyncoming.product.domain.entity.CategoryProduct;
import org.sparta.whyncoming.product.domain.entity.Product;
import org.sparta.whyncoming.product.domain.repository.CategoryRepository;
import org.sparta.whyncoming.product.domain.repository.ProductRepositoy;
import org.sparta.whyncoming.product.presentaion.dto.request.ProductRequestDto;
import org.sparta.whyncoming.product.presentaion.dto.response.ProductResponseDto;
import org.sparta.whyncoming.store.domain.entity.Store;
import org.sparta.whyncoming.store.domain.repository.StoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ProductService {

    private final ProductRepositoy productRepositoy;
    private final CategoryRepository categoryRepository;
    private final StoreRepository storeRepository;

    public ProductService(ProductRepositoy productRepositoy, CategoryRepository categoryRepository, StoreRepository storeRepository) {
        this.productRepositoy = productRepositoy;
        this.categoryRepository = categoryRepository;
        this.storeRepository = storeRepository;
    }

    public ProductResponseDto creatProduct(@RequestBody ProductRequestDto requestDto) {

        /**
         * TODO 스토어가 존재하는지에 대한 추가 검증 필요, 스토어 부분 완성되면 아래 주석된 부분 사용 예정
         */
//        Store store = storeRepository.fidByName(requestDto.getStoreName())
//                .orElseThrow(() -> new RuntimeException("존재하지 않는 가게입니다."));


        Store store = storeRepository.findByStoreName(requestDto.getStoreName())
                .orElseThrow(() -> new IllegalArgumentException("스토어 없음"));

        Product product = productRepositoy.save(new Product(store, requestDto.getProductName(), requestDto.getDescription(), requestDto.getPrice(), requestDto.getProductPictureUrl(), new ArrayList<>())); // 빈 카테고리로 생성

        List<CategoryProduct> categoryProducts = requestDto.getCategoryNames().stream()
                .map(name -> {
                    Category category = categoryRepository.findByCategoryName(name)
                            .orElseThrow(() -> new RuntimeException("Category not found: " + name));
                    return new CategoryProduct(product, category);
                })
                .toList();

        product.getCategoryProducts().addAll(categoryProducts);
        productRepositoy.save(product);
        return new ProductResponseDto(product);
    }


    /*
    TODO - 따로 response에 대한 메서드를 분리하는 것이 상품 테이블에서도 필요한가? CRUD 추가 작성 후 변경할 예정
     */
//    private ProductResponseDto toProductResponse(Product product) {
//        return new ProductResponseDto(product);
//    }
}
