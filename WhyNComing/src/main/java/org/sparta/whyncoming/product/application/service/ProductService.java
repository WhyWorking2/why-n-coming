package org.sparta.whyncoming.product.application.service;

import lombok.extern.slf4j.Slf4j;
import org.sparta.whyncoming.common.exception.BusinessException;
import org.sparta.whyncoming.common.exception.ErrorCode;
import org.sparta.whyncoming.common.s3.S3Util;
import org.sparta.whyncoming.product.domain.entity.Category;
import org.sparta.whyncoming.product.domain.entity.Product;
import org.sparta.whyncoming.product.domain.repository.CategoryRepository;
import org.sparta.whyncoming.product.domain.repository.ProductRepository;
import org.sparta.whyncoming.product.presentation.dto.request.ProductRequestDto;
import org.sparta.whyncoming.product.presentation.dto.request.ProductUpdateRequestDto;
import org.sparta.whyncoming.product.presentation.dto.response.ProductByCategoryResponseDto;
import org.sparta.whyncoming.product.presentation.dto.response.ProductDetailResponseDto;
import org.sparta.whyncoming.product.presentation.dto.response.ProductActiveResponseDto;
import org.sparta.whyncoming.product.presentation.dto.response.ProductResponseDto;
import org.sparta.whyncoming.store.domain.entity.Store;
import org.sparta.whyncoming.store.domain.repository.StoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    private final S3Util s3Util;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, StoreRepository storeRepository, S3Util s3Util) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.storeRepository = storeRepository;
        this.s3Util = s3Util;
    }

    /**
     * 상품 생성에 대한
     * @param requestDto 생성된 상품 정보
     * @return 상품ResponseDto 생성자
     */
    public ProductActiveResponseDto creatProduct(ProductRequestDto requestDto, MultipartFile productImg) {
        String productImgUrl = null;

        if(productImg != null && !productImg.isEmpty()) {
            try {
                productImgUrl = s3Util.uploadFile(productImg, "productImg");
            } catch (IOException e) {
                throw new BusinessException(ErrorCode.INVALID_REQUEST);
            }
        }

        //입점사 조회
        Store store = storeRepository.findByStoreName(requestDto.getStoreName())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND ,requestDto.getStoreName()));

        //카테고리 리스트 생성
        List<Category> categoryList = createCategoryList(requestDto.getCategoryNames());

        //상품 생성 및 저장
        Product product = productRepository.save(new Product(store, requestDto.getProductName(), requestDto.getDescription(), requestDto.getPrice(), productImgUrl, categoryList));

        return new ProductActiveResponseDto(product);
    }

    /**
     * 상품 전체 조회 - 유효한 상품만
     * @return 유효한 상품 전체 리스트 반환
     */
    @Transactional(readOnly = true)
    public List<ProductActiveResponseDto> readAllActiveProducts() {
        return productRepository.findAllActiveWithStore();
    }

    /**
     * 상품 전체 조회 - 삭제된 상품 포함
     * @return 상품 전체 리스트 반환
     */
    @Transactional(readOnly = true)
    public List<ProductResponseDto> readAllProducts() {
        return productRepository.findAllWithStore();
    }

    /**
     * 카테고리별 상품조회
     * @param categoryName 기준이 될 카테고리
     * @return 기준 카테고리에 해당되는 상품목록 리스트
     */
    @Transactional(readOnly = true)
    public List<ProductByCategoryResponseDto> readProductsByCategory(String categoryName) {
        return productRepository.findAllByCategoryName(categoryName);
    }

    /**
     * 상품 상세 조회
     * @param productId 상세조회할 상품의 UUID
     * @return 상세조회된 상품의 내역
     */
    @Transactional(readOnly = true)
    public ProductDetailResponseDto getProductDetail(UUID productId) {
        Product product = productRepository.findByProductId(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, ": 상품 없음"));

        if (product.isDeleted()) {
            throw new BusinessException(ErrorCode.NOT_FOUND, ": 상품 조회 불가");
        }

        return new ProductDetailResponseDto(product);
    }

    /**
     * 상품 수정
     * @param productId 상품의 uuid
     * @param requestDto 변경할 상품정보
     * @return 변경된 상품정보
     */
    public ProductActiveResponseDto updateProduct(UUID productId, ProductUpdateRequestDto requestDto) {
        Product product = productRepository.findByProductId(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND ,requestDto.getProductName()));

        product.update(
                requestDto.getProductName(),
                requestDto.getPrice(),
                requestDto.getDescription(),
                requestDto.getProductPictureUrl()
        );

        return new ProductActiveResponseDto(product);
    }

    //TODO 반환값 String 하드코딩 대신 쓸 타입을 정해야 하고, 이미 삭제된 상품에 대한 예외처리가 필요함
    public String deleteProduct(UUID productId) {
        Product product = productRepository.findByProductId(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));
        product.delete();

        return product.getProductId().toString();
    }

    /**
     *  입력된 카테고리 예외처리 및 카테고리 리스트화 메서드
     */
    private List<Category> createCategoryList (List<String> categoryNames) {

        //카테고리 검증
        if (categoryNames == null || categoryNames.isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, " : 카테고리");
        }

        //카테고리 조회
        List<Category> categories = categoryRepository.findAllByCategoryNameIn(categoryNames);
        if (categories.size() != categoryNames.size()) {
            throw new BusinessException(ErrorCode.NOT_FOUND, " : 카테고리");
        }

        //카테고리에 대한 리스트 생성
        List<Category> categoryList = new ArrayList<>();
        categoryList.addAll(categories);
        return categoryList;
    }
}
