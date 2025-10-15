package org.sparta.whyncoming.product.application.service;

import lombok.extern.slf4j.Slf4j;
import org.sparta.whyncoming.common.exception.BusinessException;
import org.sparta.whyncoming.common.exception.ErrorCode;
import org.sparta.whyncoming.product.domain.entity.Category;
import org.sparta.whyncoming.product.domain.entity.Product;
import org.sparta.whyncoming.product.domain.repository.CategoryRepository;
import org.sparta.whyncoming.product.domain.repository.ProductRepository;
import org.sparta.whyncoming.product.presentation.dto.request.ProductRequestDto;
import org.sparta.whyncoming.product.presentation.dto.request.ProductUpdateRequestDto;
import org.sparta.whyncoming.product.presentation.dto.response.ProductDetailResponseDto;
import org.sparta.whyncoming.product.presentation.dto.response.ProductResponseDto;
import org.sparta.whyncoming.store.domain.entity.Store;
import org.sparta.whyncoming.store.domain.repository.StoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


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
     * @param requestDto 생성된 상품 정보
     * @return 상품ResponseDto 생성자
     */
    public ProductResponseDto creatProduct(ProductRequestDto requestDto) {

        //입점사 조회
        Store store = storeRepository.findByStoreName(requestDto.getStoreName())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND ,requestDto.getStoreName()));

        //카테고리 리스트 생성
        List<Category> categoryList = createCategoryList(requestDto.getCategoryNames());

        //상품 생성
        Product product = productRepository.save(new Product(store, requestDto.getProductName(), requestDto.getDescription(), requestDto.getPrice(), requestDto.getProductPictureUrl(), categoryList));

        //저장
        productRepository.save(product);

        return new ProductResponseDto(product);
    }

    /**
     * 상품 전체 조회
     * TODO 카테고리 리스트에 대한 문제, 소프트 딜리트 조회 안되는 문제(DB에는 반영이 됨), 유저 정보에 대한 문제 미해결
     * 이후 코드 리팩토링 하면서 해결할 예정입니다.
     * @return 리스트 타입으로 상품목록 출력
     */
    @Transactional(readOnly = true)
    public List<ProductResponseDto> readAllProducts() {
        return productRepository.findAllWithStore().stream()
                .map(ProductResponseDto::new).toList();
    }

    @Transactional(readOnly = true)
    public ProductDetailResponseDto getProductDetail(UUID productId) {
        Product product = productRepository.findByProductId(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, ": 상품 없음"));

        return new ProductDetailResponseDto(product);
    }

    /**
     * 상품 수정
     * @param productId 상품의 uuid
     * @param requestDto 변경할 상품정보
     * @return 변경된 상품정보
     */
    public ProductResponseDto updateProduct(UUID productId, ProductUpdateRequestDto requestDto) {
        Product product = productRepository.findByProductId(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND ,requestDto.getProductName()));

        product.update(
                requestDto.getProductName(),
                requestDto.getPrice(),
                requestDto.getDescription(),
                requestDto.getProductPictureUrl()
        );

        Product updateProduct = productRepository.save(product);

        return new ProductResponseDto(updateProduct);
    }

    //TODO 반환값 String 하드코딩 대신 쓸 타입을 정해야 하고, 이미 삭제된 상품에 대한 예외처리가 필요함
    public String deleteProduct(UUID productId) {
        Product product = productRepository.findByProductId(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));
        product.delete();


        //TODO 이 부분 트랜잭션 영속성 있어서 save 없어도 진행 될 수도 있을 거 같아서 없앨지 고민중입니다.
        Product deletedProduct = productRepository.save(product);

        return deletedProduct.getProductId().toString();
    }

    /**
     *  입력된 카테고리 예외처리 및 카테고리 리스트화 메서드
     */
    private List<Category> createCategoryList (List<String> categoryName) {

        //카테고리 검증
        if (categoryName == null || categoryName.isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, " : 카테고리");
        }

        //카테고리 조회
        List<Category> categories = categoryRepository.findAllByCategoryNameIn(categoryName);
        if (categories.size() != categoryName.size()) {
            throw new BusinessException(ErrorCode.NOT_FOUND, " : 카테고리");
        }

        //카테고리에 대한 리스트 생성
        List<Category> categoryList = new ArrayList<>();
        categoryList.addAll(categories);
        return categoryList;
    }

}
