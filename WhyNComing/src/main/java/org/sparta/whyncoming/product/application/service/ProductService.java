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
import org.sparta.whyncoming.user.domain.entity.User;
import org.sparta.whyncoming.user.domain.enums.Role;
import org.sparta.whyncoming.user.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
public class ProductService {

    private final ProductRepositoy productRepositoy;
    private final CategoryRepository categoryRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    public ProductService(ProductRepositoy productRepositoy, CategoryRepository categoryRepository, StoreRepository storeRepository, UserRepository userRepository) {
        this.productRepositoy = productRepositoy;
        this.categoryRepository = categoryRepository;
        this.storeRepository = storeRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public ProductResponseDto creatProduct(@RequestBody ProductRequestDto requestDto) {

        /**
         * TODO 스토어가 존재하는지에 대한 추가 검증 필요, 스토어 부분 완성되면 아래 주석된 부분 사용 예정
         */
//        Store store = storeRepository.fidByName(requestDto.getStoreName())
//                .orElseThrow(() -> new RuntimeException("존재하지 않는 가게입니다."));

        /**
         * 테스트 중 문제 발생 시 사용할 더미
         * TODO 나중에 이 부분 더미 지우기 必
         */
//       User dummyUser = new User(
//                "storeOwner01",
//                "1234", // 테스트용 비밀번호 (암호화 X)
//                "김사장",
//                "01011112222",
//                "owner01@test.com",
//                Role.CUSTOMER,
//                new ArrayList<>(), // addresses
//                new ArrayList<>(), // stores
//                new ArrayList<>(), // orders
//                new ArrayList<>(), // reviews
//                new ArrayList<>(), // ownerReviews
//                new ArrayList<>()  // carts
//        );
//
//        userRepository.save(dummyUser);
//
//        // 2️⃣ 더미 스토어 생성
//        Store dummyStore = new Store(
//                dummyUser,
//                "홍콩반점",
//                "서울특별시 강남구 역삼동 123-45",
//                "https://example.com/logo.png",
//                "025557777",
//                "맛있고 바삭한 수제치킨 전문점",
//                15000,
//                2000,
//                "11:00 ~ 23:00",
//                "서울 강남 전 지역 배달 가능",
//                new ArrayList<>(), // storeImages
//                new ArrayList<>(), // products
//                new ArrayList<>(), // orders
//                new ArrayList<>(), // reviews
//                new ArrayList<>(), // carts
//                new ArrayList<>()  // categoryStores
//        );
//
//        storeRepository.save(dummyStore);
//
//        // 3️⃣ 손님(주문자) 생성
//        User customer = new User(
//                "customer01",
//                "1234",
//                "김손님",
//                "01099998888",
//                "customer01@test.com",
//                Role.CUSTOMER,
//                new ArrayList<>(), // addresses
//                new ArrayList<>(), // stores
//                new ArrayList<>(), // orders
//                new ArrayList<>(), // reviews
//                new ArrayList<>(), // ownerReviews
//                new ArrayList<>()  // carts
//        );
//        userRepository.save(customer);
//
//        Category chineseCategory = new Category("중식" ,new ArrayList<>(),new ArrayList<>());
//        categoryRepository.save(chineseCategory);

        /**
         * 여기까지 더미
         */

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

    public List<ProductResponseDto> readAllProducts() {

        List<ProductResponseDto> responseDtoList = productRepositoy.findAll().stream()
                .map(product -> new ProductResponseDto(
                        product.getProductSeq(),
                        product.getStore().getStoreName(),
                        product.getProductName(),
                        product.getPrice(),
                        product.getProductPictureUrl()
                )).toList();
        return responseDtoList;
    }




    /*
    TODO - 따로 response에 대한 메서드를 분리하는 것이 상품 테이블에서도 필요한가? CRUD 추가 작성 후 변경할 예정
     */
//    private ProductResponseDto toProductResponse(Product product) {
//        return new ProductResponseDto(product);
//    }
}
