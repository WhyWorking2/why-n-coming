-- 필요한 확장
CREATE EXTENSION IF NOT EXISTS pgcrypto;
----------------------------
-- 1) 사용자 (users)
----------------------------
INSERT INTO users (
    user_no, user_id, password, user_name, user_phone, email, role,
    created_date, modified_date, deleted_date,
    created_by, modified_by, deleted_by
)
VALUES
    (1, 'user01', '$2a$10$KazjRgX87iwb2gHQa1Fqs.9CCvk9f6RuY3IslR0UGfOAcsB6QvDrC', '홍길동', '01011110001', 'user01@test.com', 'MANAGER',  NOW(), NOW(), NULL, 1, 1, NULL),
    (2, 'user02', '$2a$10$nZBkoWM9IxG5bXPt1KW3Ve3qK3XLBRJyOaujbchtC/Ua3G07y5ga2', '김철수', '01011110002', 'user02@test.com', 'OWNER',    NOW(), NOW(), NULL, 2, 2, NULL),
    (3, 'user03', '$2a$10$ZaVcSOdUXG/yVfneM24QX.z1mLwUCwhTgWYTXoJExkKbh09YUeIAe', '이영희', '01011110003', 'user03@test.com', 'MASTER',   NOW(), NOW(), NULL, 3, 3, NULL),
    (4, 'user04', '$2a$10$bDwwFnP7s.jQ8DXDLwGZY.d6J208eiNw14d3t8Z2gj/LcpQjbp5DO', '박민수', '01011110004', 'user04@test.com', 'CUSTOMER', NOW(), NOW(), NULL, 4, 4, NULL),
    (5, 'user05', '$2a$10$S0rYQ6tgCGegmSxpQ.em5e4k2htnCUFwPAa78iUWkRHIlKtv7CCNm', '최지현', '01011110005', 'user05@test.com', 'CUSTOMER', NOW(), NOW(), NULL, 5, 5, NULL),
    (6, 'user06', '$2a$10$r5z434.rcJMd4jOidj2IM.XBAoBFqLMyzUh7JGH4T5aM37cQJEXvy', '정우성', '01011110006', 'user06@test.com', 'CUSTOMER', NOW(), NOW(), NULL, 6, 6, NULL),
    (7, 'user07', '$2a$10$7k3TIpb66a0pDcHRXpr3r.0ExVbv5fmZMYpFg0sSGfpE9o95gsQuO', '한가인', '01011110007', 'user07@test.com', 'CUSTOMER', NOW(), NOW(), NULL, 7, 7, NULL),
    (8, 'user08', '$2a$10$hcplJBuuPDZdcYoRvb/IyeICAH10wpikr7pFj6g68k0xa4VpvdQA6', '조세호', '01011110008', 'user08@test.com', 'CUSTOMER', NOW(), NOW(), NULL, 8, 8, NULL),
    (9, 'user09', '$2a$10$1SYipAQMnkExCoJ9B4Tyru1jgXtMvLUTKAUE3YPhgVQ41nM8Fz/Lu', '유재석', '01011110009', 'user09@test.com', 'CUSTOMER', NOW(), NOW(), NULL, 9, 9, NULL),
    (10,'user10', '$2a$10$.6VDxIkE1lYEP05rCxY.l.HsnX5onEx4h5xa4Y.v8Ee3LLwxaK7l2', '강호동', '01011110010', 'user10@test.com', 'CUSTOMER', NOW(), NOW(), NULL, 10, 10, NULL),
    (11,'user11', '$2a$10$FQmI2GcJ8Xq7vlmFzES5OO4TETIB7DQa9mld9SJIInwi.gHOPhh7i', '이순신', '01011110011', 'user11@test.com', 'OWNER',    NOW(), NOW(), NULL, 11, 11, NULL);
----------------------------
-- 2) 코드→UUID 매핑 테이블들 (임시)
----------------------------
CREATE TEMP TABLE addr_map  (code text PRIMARY KEY, id uuid NOT NULL);
CREATE TEMP TABLE store_map (code text PRIMARY KEY, id uuid NOT NULL);
CREATE TEMP TABLE prod_map  (code text PRIMARY KEY, id uuid NOT NULL);
CREATE TEMP TABLE cat_map   (code text PRIMARY KEY, id uuid NOT NULL);
CREATE TEMP TABLE order_map (code text PRIMARY KEY, id uuid NOT NULL);

INSERT INTO addr_map (code, id) VALUES
                                    ('ADDR001', gen_random_uuid()), ('ADDR002', gen_random_uuid()), ('ADDR003', gen_random_uuid()),
                                    ('ADDR004', gen_random_uuid()), ('ADDR005', gen_random_uuid()), ('ADDR006', gen_random_uuid()),
                                    ('ADDR007', gen_random_uuid()), ('ADDR008', gen_random_uuid()), ('ADDR009', gen_random_uuid()),
                                    ('ADDR010', gen_random_uuid()), ('ADDR011', gen_random_uuid());

INSERT INTO store_map (code, id) VALUES
                                     ('STORE001', gen_random_uuid()), ('STORE002', gen_random_uuid()), ('STORE003', gen_random_uuid()),
                                     ('STORE004', gen_random_uuid()), ('STORE005', gen_random_uuid()), ('STORE006', gen_random_uuid()),
                                     ('STORE007', gen_random_uuid()), ('STORE008', gen_random_uuid()), ('STORE009', gen_random_uuid()),
                                     ('STORE010', gen_random_uuid());

INSERT INTO prod_map (code, id) VALUES
                                    ('PROD001', gen_random_uuid()), ('PROD002', gen_random_uuid()), ('PROD003', gen_random_uuid()),
                                    ('PROD004', gen_random_uuid()), ('PROD005', gen_random_uuid()), ('PROD006', gen_random_uuid()),
                                    ('PROD007', gen_random_uuid()), ('PROD008', gen_random_uuid()), ('PROD009', gen_random_uuid()),
                                    ('PROD010', gen_random_uuid()), ('PROD011', gen_random_uuid()), ('PROD012', gen_random_uuid()),
                                    ('PROD013', gen_random_uuid()), ('PROD014', gen_random_uuid()), ('PROD015', gen_random_uuid()),
                                    ('PROD016', gen_random_uuid()), ('PROD017', gen_random_uuid()), ('PROD018', gen_random_uuid()),
                                    ('PROD019', gen_random_uuid()), ('PROD020', gen_random_uuid());

INSERT INTO cat_map (code, id) VALUES
                                   ('CAT001', gen_random_uuid()), ('CAT002', gen_random_uuid()), ('CAT003', gen_random_uuid()),
                                   ('CAT004', gen_random_uuid()), ('CAT005', gen_random_uuid()), ('CAT006', gen_random_uuid()),
                                   ('CAT007', gen_random_uuid()), ('CAT008', gen_random_uuid()), ('CAT009', gen_random_uuid()),
                                   ('CAT010', gen_random_uuid());

INSERT INTO order_map (code, id) VALUES
                                     ('ORDER001', gen_random_uuid()), ('ORDER002', gen_random_uuid()), ('ORDER003', gen_random_uuid()),
                                     ('ORDER004', gen_random_uuid()), ('ORDER005', gen_random_uuid()), ('ORDER006', gen_random_uuid()),
                                     ('ORDER007', gen_random_uuid()), ('ORDER008', gen_random_uuid()), ('ORDER009', gen_random_uuid()),
                                     ('ORDER010', gen_random_uuid());

----------------------------
-- 3) 주소 (addresses)  ※ created_date/modified_date 필수
----------------------------
INSERT INTO addresses (address_id, address, user_no, created_date, modified_date, representative_yn, deleted_date)
VALUES
    ((SELECT id FROM addr_map WHERE code='ADDR001'), '서울 강남구 1번지',  1, NOW(), NOW(), 'Y', NULL),
    ((SELECT id FROM addr_map WHERE code='ADDR002'), '서울 마포구 2번지',  2, NOW(), NOW(), 'Y', NULL),
    ((SELECT id FROM addr_map WHERE code='ADDR003'), '서울 서초구 3번지',  3, NOW(), NOW(), 'Y', NULL),
    ((SELECT id FROM addr_map WHERE code='ADDR004'), '서울 송파구 4번지',  4, NOW(), NOW(), 'Y', NULL),
    ((SELECT id FROM addr_map WHERE code='ADDR005'), '서울 동작구 5번지',  5, NOW(), NOW(), 'Y', NULL),
    ((SELECT id FROM addr_map WHERE code='ADDR006'), '서울 은평구 6번지',  6, NOW(), NOW(), 'Y', NULL),
    ((SELECT id FROM addr_map WHERE code='ADDR007'), '서울 구로구 7번지',  7, NOW(), NOW(), 'Y', NULL),
    ((SELECT id FROM addr_map WHERE code='ADDR008'), '서울 성북구 8번지',  8, NOW(), NOW(), 'Y', NULL),
    ((SELECT id FROM addr_map WHERE code='ADDR009'), '서울 강북구 9번지',  9, NOW(), NOW(), 'Y', NULL),
    ((SELECT id FROM addr_map WHERE code='ADDR010'),'서울 강서구 10번지', 10, NOW(), NOW(), 'Y', NULL),
    ((SELECT id FROM addr_map WHERE code='ADDR011'),'서울 강서구 11번지', 11, NOW(), NOW(), 'Y', NULL);

----------------------------
-- 4) 매장 (stores)  ※ store_id(uuid), delivery_tip 필수
----------------------------
INSERT INTO stores (
    store_id, user_no, store_name, store_address, store_logo_url, store_phone, store_content,
    min_delivery_price, delivery_tip, store_rating, store_review_count, operation_hours,
    delivery_address, created_date, modified_date
)
VALUES
    ((SELECT id FROM store_map WHERE code='STORE001'), 2, '치킨나라1', '강남구', 'logo1.png',  '02-111-0001', '치킨 전문점', 15000, 3000, 4.2, 11, '10:00-22:00', '강남구', NOW(), NOW()),
    ((SELECT id FROM store_map WHERE code='STORE002'), 2, '치킨나라2', '마포구', 'logo2.png',  '02-111-0002', '치킨 전문점', 16000, 3000, 4.5, 15, '10:00-22:00', '마포구', NOW(), NOW()),
    ((SELECT id FROM store_map WHERE code='STORE003'),11, '피자스쿨1', '서초구', 'logo3.png',  '02-111-0003', '피자 전문점', 20000, 3000, 4.1, 10, '11:00-23:00', '서초구', NOW(), NOW()),
    ((SELECT id FROM store_map WHERE code='STORE004'), 2, '피자스쿨2', '송파구', 'logo4.png',  '02-111-0004', '피자 전문점', 21000, 3000, 4.7, 18, '11:00-23:00', '송파구', NOW(), NOW()),
    ((SELECT id FROM store_map WHERE code='STORE005'), 2, '햄버거킹', '동작구', 'logo5.png',  '02-111-0005', '햄버거 전문점', 12000, 3000, 4.3, 20, '09:00-22:00', '동작구', NOW(), NOW()),
    ((SELECT id FROM store_map WHERE code='STORE006'),11, '중국집1',  '은평구', 'logo6.png',  '02-111-0006', '중식 전문점', 13000, 3000, 4.0,  9, '10:00-21:00', '은평구', NOW(), NOW()),
    ((SELECT id FROM store_map WHERE code='STORE007'),11, '중국집2',  '구로구', 'logo7.png',  '02-111-0007', '중식 전문점', 14000, 3000, 3.9, 12, '10:00-21:00', '구로구', NOW(), NOW()),
    ((SELECT id FROM store_map WHERE code='STORE008'),11, '일식집1',  '성북구', 'logo8.png',  '02-111-0008', '일식 전문점', 18000, 3000, 4.6, 16, '11:00-22:00', '성북구', NOW(), NOW()),
    ((SELECT id FROM store_map WHERE code='STORE009'), 2, '분식집1',  '강북구', 'logo9.png',  '02-111-0009', '분식 전문점',  8000, 3000, 4.4, 25, '09:00-20:00', '강북구', NOW(), NOW()),
    ((SELECT id FROM store_map WHERE code='STORE010'), 2, '분식집2',  '강서구', 'logo10.png', '02-111-0010', '분식 전문점',  9000, 3000, 4.5, 30, '09:00-20:00', '강서구', NOW(), NOW());

----------------------------
-- 5) 상품 (products)  ※ store_id FK
----------------------------
INSERT INTO products (product_id, store_id, product_name, description, price, product_picture_url, created_date, modified_date)
VALUES
    ((SELECT id FROM prod_map  WHERE code='PROD001'), (SELECT id FROM store_map WHERE code='STORE001'), '후라이드 치킨',       '바삭한 후라이드', 18000, 'ch1.jpg', NOW(), NOW()),
    ((SELECT id FROM prod_map  WHERE code='PROD002'), (SELECT id FROM store_map WHERE code='STORE001'), '양념 치킨',           '매콤달콤 양념',   19000, 'ch2.jpg', NOW(), NOW()),
    ((SELECT id FROM prod_map  WHERE code='PROD003'), (SELECT id FROM store_map WHERE code='STORE002'), '간장 치킨',           '짭쪼름 간장맛',   20000, 'ch3.jpg', NOW(), NOW()),
    ((SELECT id FROM prod_map  WHERE code='PROD004'), (SELECT id FROM store_map WHERE code='STORE002'), '마늘 치킨',           '마늘향 가득',     21000, 'ch4.jpg', NOW(), NOW()),
    ((SELECT id FROM prod_map  WHERE code='PROD005'), (SELECT id FROM store_map WHERE code='STORE003'), '페퍼로니 피자',       '진한 치즈',       22000, 'pz1.jpg', NOW(), NOW()),
    ((SELECT id FROM prod_map  WHERE code='PROD006'), (SELECT id FROM store_map WHERE code='STORE003'), '치즈 피자',           '치즈 듬뿍',       23000, 'pz2.jpg', NOW(), NOW()),
    ((SELECT id FROM prod_map  WHERE code='PROD007'), (SELECT id FROM store_map WHERE code='STORE004'), '불고기 피자',         '한국식 피자',     24000, 'pz3.jpg', NOW(), NOW()),
    ((SELECT id FROM prod_map  WHERE code='PROD008'), (SELECT id FROM store_map WHERE code='STORE004'), '콤비네이션 피자',     '토핑 풍성',       25000, 'pz4.jpg', NOW(), NOW()),
    ((SELECT id FROM prod_map  WHERE code='PROD009'), (SELECT id FROM store_map WHERE code='STORE005'), '치즈버거',             '치즈 가득',        9000, 'bg1.jpg', NOW(), NOW()),
    ((SELECT id FROM prod_map  WHERE code='PROD010'), (SELECT id FROM store_map WHERE code='STORE005'), '불고기버거',           '불고기 소스',     10000, 'bg2.jpg', NOW(), NOW()),
    ((SELECT id FROM prod_map  WHERE code='PROD011'), (SELECT id FROM store_map WHERE code='STORE006'), '짜장면',               '달달한 짜장',       7000, 'cn1.jpg', NOW(), NOW()),
    ((SELECT id FROM prod_map  WHERE code='PROD012'), (SELECT id FROM store_map WHERE code='STORE006'), '짬뽕',                 '얼큰한 짬뽕',       8000, 'cn2.jpg', NOW(), NOW()),
    ((SELECT id FROM prod_map  WHERE code='PROD013'), (SELECT id FROM store_map WHERE code='STORE007'), '탕수육',               '바삭 탕수육',      15000, 'cn3.jpg', NOW(), NOW()),
    ((SELECT id FROM prod_map  WHERE code='PROD014'), (SELECT id FROM store_map WHERE code='STORE007'), '군만두',               '노릇 만두',         6000, 'cn4.jpg', NOW(), NOW()),
    ((SELECT id FROM prod_map  WHERE code='PROD015'), (SELECT id FROM store_map WHERE code='STORE008'), '초밥세트',             '신선한 초밥',      20000, 'jp1.jpg', NOW(), NOW()),
    ((SELECT id FROM prod_map  WHERE code='PROD016'), (SELECT id FROM store_map WHERE code='STORE008'), '우동',                 '시원한 국물',        9000, 'jp2.jpg', NOW(), NOW()),
    ((SELECT id FROM prod_map  WHERE code='PROD017'), (SELECT id FROM store_map WHERE code='STORE009'), '떡볶이',               '매콤떡볶이',         5000, 'sn1.jpg', NOW(), NOW()),
    ((SELECT id FROM prod_map  WHERE code='PROD018'), (SELECT id FROM store_map WHERE code='STORE009'), '김밥',                 '든든김밥',           4000, 'sn2.jpg', NOW(), NOW()),
    ((SELECT id FROM prod_map  WHERE code='PROD019'), (SELECT id FROM store_map WHERE code='STORE010'), '라면',                 '진한라면',           4000, 'sn3.jpg', NOW(), NOW()),
    ((SELECT id FROM prod_map  WHERE code='PROD020'), (SELECT id FROM store_map WHERE code='STORE010'), '순대',                 '쫄깃순대',           6000, 'sn4.jpg', NOW(), NOW());

----------------------------
-- 6) 카테고리 (categories)  ※ created/modified 필수
----------------------------
INSERT INTO categories (category_id, category_name, created_date, modified_date, deleted_date)
VALUES
    ((SELECT id FROM cat_map WHERE code='CAT001'), '치킨',  NOW(), NOW(), NULL),
    ((SELECT id FROM cat_map WHERE code='CAT002'), '피자',  NOW(), NOW(), NULL),
    ((SELECT id FROM cat_map WHERE code='CAT003'), '햄버거',NOW(), NOW(), NULL),
    ((SELECT id FROM cat_map WHERE code='CAT004'), '중식',  NOW(), NOW(), NULL),
    ((SELECT id FROM cat_map WHERE code='CAT005'), '일식',  NOW(), NOW(), NULL),
    ((SELECT id FROM cat_map WHERE code='CAT006'), '분식',  NOW(), NOW(), NULL),
    ((SELECT id FROM cat_map WHERE code='CAT007'), '디저트',NOW(), NOW(), NULL),
    ((SELECT id FROM cat_map WHERE code='CAT008'), '커피',  NOW(), NOW(), NULL),
    ((SELECT id FROM cat_map WHERE code='CAT009'), '한식',  NOW(), NOW(), NULL),
    ((SELECT id FROM cat_map WHERE code='CAT010'), '야식',  NOW(), NOW(), NULL);

----------------------------
-- 7) 카테고리-매장 연결 (category_stores)
--    PK 이름: category_product_id (스키마 기준), created/modified 필수
----------------------------
INSERT INTO category_stores (category_stores_id, store_id, category_id, created_date, modified_date, deleted_date)
VALUES
    (gen_random_uuid(), (SELECT id FROM store_map WHERE code='STORE001'), (SELECT id FROM cat_map WHERE code='CAT001'), NOW(), NOW(), NULL),
    (gen_random_uuid(), (SELECT id FROM store_map WHERE code='STORE002'), (SELECT id FROM cat_map WHERE code='CAT001'), NOW(), NOW(), NULL),
    (gen_random_uuid(), (SELECT id FROM store_map WHERE code='STORE003'), (SELECT id FROM cat_map WHERE code='CAT002'), NOW(), NOW(), NULL),
    (gen_random_uuid(), (SELECT id FROM store_map WHERE code='STORE004'), (SELECT id FROM cat_map WHERE code='CAT002'), NOW(), NOW(), NULL),
    (gen_random_uuid(), (SELECT id FROM store_map WHERE code='STORE005'), (SELECT id FROM cat_map WHERE code='CAT003'), NOW(), NOW(), NULL),
    (gen_random_uuid(), (SELECT id FROM store_map WHERE code='STORE006'), (SELECT id FROM cat_map WHERE code='CAT004'), NOW(), NOW(), NULL),
    (gen_random_uuid(), (SELECT id FROM store_map WHERE code='STORE007'), (SELECT id FROM cat_map WHERE code='CAT004'), NOW(), NOW(), NULL),
    (gen_random_uuid(), (SELECT id FROM store_map WHERE code='STORE008'), (SELECT id FROM cat_map WHERE code='CAT005'), NOW(), NOW(), NULL),
    (gen_random_uuid(), (SELECT id FROM store_map WHERE code='STORE009'), (SELECT id FROM cat_map WHERE code='CAT006'), NOW(), NOW(), NULL),
    (gen_random_uuid(), (SELECT id FROM store_map WHERE code='STORE010'), (SELECT id FROM cat_map WHERE code='CAT006'), NOW(), NOW(), NULL);

----------------------------
-- 8) 카테고리-상품 연결 (category_products)
--    PK 이름: categorystore_id (스키마 기준), created/modified 필수
----------------------------
INSERT INTO category_products (category_product_id, product_id, category_id, created_date, modified_date, deleted_date)
VALUES
    (gen_random_uuid(), (SELECT id FROM prod_map WHERE code='PROD001'), (SELECT id FROM cat_map WHERE code='CAT001'), NOW(), NOW(), NULL),
    (gen_random_uuid(), (SELECT id FROM prod_map WHERE code='PROD002'), (SELECT id FROM cat_map WHERE code='CAT001'), NOW(), NOW(), NULL),
    (gen_random_uuid(), (SELECT id FROM prod_map WHERE code='PROD005'), (SELECT id FROM cat_map WHERE code='CAT002'), NOW(), NOW(), NULL),
    (gen_random_uuid(), (SELECT id FROM prod_map WHERE code='PROD006'), (SELECT id FROM cat_map WHERE code='CAT002'), NOW(), NOW(), NULL),
    (gen_random_uuid(), (SELECT id FROM prod_map WHERE code='PROD009'), (SELECT id FROM cat_map WHERE code='CAT003'), NOW(), NOW(), NULL),
    (gen_random_uuid(), (SELECT id FROM prod_map WHERE code='PROD010'), (SELECT id FROM cat_map WHERE code='CAT003'), NOW(), NOW(), NULL),
    (gen_random_uuid(), (SELECT id FROM prod_map WHERE code='PROD011'), (SELECT id FROM cat_map WHERE code='CAT004'), NOW(), NOW(), NULL),
    (gen_random_uuid(), (SELECT id FROM prod_map WHERE code='PROD012'), (SELECT id FROM cat_map WHERE code='CAT004'), NOW(), NOW(), NULL),
    (gen_random_uuid(), (SELECT id FROM prod_map WHERE code='PROD015'), (SELECT id FROM cat_map WHERE code='CAT005'), NOW(), NOW(), NULL),
    (gen_random_uuid(), (SELECT id FROM prod_map WHERE code='PROD017'), (SELECT id FROM cat_map WHERE code='CAT006'), NOW(), NOW(), NULL),
    (gen_random_uuid(), (SELECT id FROM prod_map WHERE code='PROD019'), (SELECT id FROM cat_map WHERE code='CAT006'), NOW(), NOW(), NULL),
    (gen_random_uuid(), (SELECT id FROM prod_map WHERE code='PROD020'), (SELECT id FROM cat_map WHERE code='CAT006'), NOW(), NOW(), NULL);

----------------------------
-- 9) 주문 (orders)  ※ status 체크값 준수
----------------------------
INSERT INTO orders (order_id, store_id, user_no, payment_method, total_price, requests, created_date, modified_date, status, deleted_date)
VALUES
    ((SELECT id FROM order_map WHERE code='ORDER001'), (SELECT id FROM store_map WHERE code='STORE001'),  5, 'CARD', 18000 * 2, '빨리 배달 부탁드립니다', NOW(), NOW(), 'SUCCESS', NULL),
    ((SELECT id FROM order_map WHERE code='ORDER002'), (SELECT id FROM store_map WHERE code='STORE002'),  4, 'CASH', 19000 * 1, '빨리 주세요',         NOW(), NOW(), 'SUCCESS', NULL),
    ((SELECT id FROM order_map WHERE code='ORDER003'), (SELECT id FROM store_map WHERE code='STORE003'),  7, 'CARD', 20000 * 3, '포장 부탁',           NOW(), NOW(), 'SUCCESS', NULL),
    ((SELECT id FROM order_map WHERE code='ORDER004'), (SELECT id FROM store_map WHERE code='STORE004'),  4, 'CARD', 21000 * 1, '빨리요',               NOW(), NOW(), 'SUCCESS', NULL),
    ((SELECT id FROM order_map WHERE code='ORDER005'), (SELECT id FROM store_map WHERE code='STORE005'),  5, 'CASH', 22000 * 2, '포장 부탁',           NOW(), NOW(), 'SUCCESS', NULL),
    ((SELECT id FROM order_map WHERE code='ORDER006'), (SELECT id FROM store_map WHERE code='STORE006'),  6, 'CARD', 23000 * 2, '덜 맵게',             NOW(), NOW(), 'SUCCESS', NULL),
    ((SELECT id FROM order_map WHERE code='ORDER007'), (SELECT id FROM store_map WHERE code='STORE007'),  7, 'CARD', 24000 * 1, '소스 많이',           NOW(), NOW(), 'SUCCESS', NULL),
    ((SELECT id FROM order_map WHERE code='ORDER008'), (SELECT id FROM store_map WHERE code='STORE008'),  8, 'CASH', 25000 * 3, '단무지 추가',         NOW(), NOW(), 'SUCCESS', NULL),
    ((SELECT id FROM order_map WHERE code='ORDER009'), (SELECT id FROM store_map WHERE code='STORE009'),  9, 'CARD',  9000 * 4, '빨리 주세요',         NOW(), NOW(), 'SUCCESS', NULL),
    ((SELECT id FROM order_map WHERE code='ORDER010'), (SELECT id FROM store_map WHERE code='STORE010'), 10, 'CARD', 10000 * 2, '포장해 주세요',       NOW(), NOW(), 'SUCCESS', NULL);

----------------------------
-- 10) 장바구니 (carts)  ※ user_no 필수 → 주문의 user_no 사용
----------------------------
INSERT INTO carts (cart_id, store_id, order_id, product_id, quantity, user_no, created_date, modified_date)
VALUES
    (gen_random_uuid(), (SELECT id FROM store_map WHERE code='STORE001'), (SELECT id FROM order_map WHERE code='ORDER001'), (SELECT id FROM prod_map WHERE code='PROD001'), 2,
     (SELECT user_no FROM orders WHERE order_id = (SELECT id FROM order_map WHERE code='ORDER001')), NOW(), NOW()),
    (gen_random_uuid(), (SELECT id FROM store_map WHERE code='STORE002'), (SELECT id FROM order_map WHERE code='ORDER002'), (SELECT id FROM prod_map WHERE code='PROD002'), 1,
     (SELECT user_no FROM orders WHERE order_id = (SELECT id FROM order_map WHERE code='ORDER002')), NOW(), NOW()),
    (gen_random_uuid(), (SELECT id FROM store_map WHERE code='STORE003'), (SELECT id FROM order_map WHERE code='ORDER003'), (SELECT id FROM prod_map WHERE code='PROD003'), 3,
     (SELECT user_no FROM orders WHERE order_id = (SELECT id FROM order_map WHERE code='ORDER003')), NOW(), NOW()),
    (gen_random_uuid(), (SELECT id FROM store_map WHERE code='STORE004'), (SELECT id FROM order_map WHERE code='ORDER004'), (SELECT id FROM prod_map WHERE code='PROD004'), 1,
     (SELECT user_no FROM orders WHERE order_id = (SELECT id FROM order_map WHERE code='ORDER004')), NOW(), NOW()),
    (gen_random_uuid(), (SELECT id FROM store_map WHERE code='STORE005'), (SELECT id FROM order_map WHERE code='ORDER005'), (SELECT id FROM prod_map WHERE code='PROD005'), 2,
     (SELECT user_no FROM orders WHERE order_id = (SELECT id FROM order_map WHERE code='ORDER005')), NOW(), NOW()),
    (gen_random_uuid(), (SELECT id FROM store_map WHERE code='STORE006'), (SELECT id FROM order_map WHERE code='ORDER006'), (SELECT id FROM prod_map WHERE code='PROD006'), 2,
     (SELECT user_no FROM orders WHERE order_id = (SELECT id FROM order_map WHERE code='ORDER006')), NOW(), NOW()),
    (gen_random_uuid(), (SELECT id FROM store_map WHERE code='STORE007'), (SELECT id FROM order_map WHERE code='ORDER007'), (SELECT id FROM prod_map WHERE code='PROD007'), 1,
     (SELECT user_no FROM orders WHERE order_id = (SELECT id FROM order_map WHERE code='ORDER007')), NOW(), NOW()),
    (gen_random_uuid(), (SELECT id FROM store_map WHERE code='STORE008'), (SELECT id FROM order_map WHERE code='ORDER008'), (SELECT id FROM prod_map WHERE code='PROD008'), 3,
     (SELECT user_no FROM orders WHERE order_id = (SELECT id FROM order_map WHERE code='ORDER008')), NOW(), NOW()),
    (gen_random_uuid(), (SELECT id FROM store_map WHERE code='STORE009'), (SELECT id FROM order_map WHERE code='ORDER009'), (SELECT id FROM prod_map WHERE code='PROD009'), 4,
     (SELECT user_no FROM orders WHERE order_id = (SELECT id FROM order_map WHERE code='ORDER009')), NOW(), NOW()),
    (gen_random_uuid(), (SELECT id FROM store_map WHERE code='STORE010'), (SELECT id FROM order_map WHERE code='ORDER010'), (SELECT id FROM prod_map WHERE code='PROD010'), 2,
     (SELECT user_no FROM orders WHERE order_id = (SELECT id FROM order_map WHERE code='ORDER010')), NOW(), NOW());

-- 가격 정합성 보정
UPDATE orders o
SET total_price = c.total
    FROM (
    SELECT o.order_id, SUM(p.price * c.quantity) AS total
    FROM orders o
    JOIN carts c   ON o.order_id = c.order_id
    JOIN products p ON c.product_id = p.product_id
    GROUP BY o.order_id
) c
WHERE o.order_id = c.order_id;

----------------------------
-- 11) 배달 (deliveries)  ※ delivery_status 체크값 매핑
-- '배송중'→'DELIVERING', '배송완료'→'DELIVERED', '대기중'→'ACCEPTED'
----------------------------
INSERT INTO deliveries (delivery_id, order_id, user_no, address_id, delivery_status, deliveryPosition, created_date, modified_date, deleted_date)
VALUES
    (gen_random_uuid(), (SELECT id FROM order_map WHERE code='ORDER001'),  1, (SELECT id FROM addr_map WHERE code='ADDR001'), 'DELIVERING', '강남대로 10',  NOW(), NOW(), NULL),
    (gen_random_uuid(), (SELECT id FROM order_map WHERE code='ORDER002'),  2, (SELECT id FROM addr_map WHERE code='ADDR002'), 'DELIVERED',  '마포대로 20',  NOW(), NOW(), NULL),
    (gen_random_uuid(), (SELECT id FROM order_map WHERE code='ORDER003'),  3, (SELECT id FROM addr_map WHERE code='ADDR003'), 'ACCEPTED',   '서초대로 30',  NOW(), NOW(), NULL),
    (gen_random_uuid(), (SELECT id FROM order_map WHERE code='ORDER004'),  4, (SELECT id FROM addr_map WHERE code='ADDR004'), 'DELIVERING', '송파대로 40',  NOW(), NOW(), NULL),
    (gen_random_uuid(), (SELECT id FROM order_map WHERE code='ORDER005'),  5, (SELECT id FROM addr_map WHERE code='ADDR005'), 'DELIVERED',  '동작대로 50',  NOW(), NOW(), NULL),
    (gen_random_uuid(), (SELECT id FROM order_map WHERE code='ORDER006'),  6, (SELECT id FROM addr_map WHERE code='ADDR006'), 'DELIVERING', '은평대로 60',  NOW(), NOW(), NULL),
    (gen_random_uuid(), (SELECT id FROM order_map WHERE code='ORDER007'),  7, (SELECT id FROM addr_map WHERE code='ADDR007'), 'DELIVERED',  '구로대로 70',  NOW(), NOW(), NULL),
    (gen_random_uuid(), (SELECT id FROM order_map WHERE code='ORDER008'),  8, (SELECT id FROM addr_map WHERE code='ADDR008'), 'ACCEPTED',   '성북대로 80',  NOW(), NOW(), NULL),
    (gen_random_uuid(), (SELECT id FROM order_map WHERE code='ORDER009'),  9, (SELECT id FROM addr_map WHERE code='ADDR009'), 'DELIVERING', '강북대로 90',  NOW(), NOW(), NULL),
    (gen_random_uuid(), (SELECT id FROM order_map WHERE code='ORDER010'), 10, (SELECT id FROM addr_map WHERE code='ADDR010'), 'DELIVERED',  '강서대로 100', NOW(), NOW(), NULL);

----------------------------
-- 12) 리뷰 (reviews)
----------------------------
INSERT INTO reviews (review_id, order_id, store_id, user_no, review_rating, review_content, review_picture_url, created_date, modified_date, deleted_date)
VALUES
    (gen_random_uuid(), (SELECT id FROM order_map WHERE code='ORDER001'), (SELECT id FROM store_map WHERE code='STORE001'),  5, 5, '치킨이 정말 맛있어요!', 'rev1.jpg',  NOW(), NOW(), NULL),
    (gen_random_uuid(), (SELECT id FROM order_map WHERE code='ORDER002'), (SELECT id FROM store_map WHERE code='STORE002'),  4, 4, '피자 맛있습니다.',   'rev2.jpg',  NOW(), NOW(), NULL),
    (gen_random_uuid(), (SELECT id FROM order_map WHERE code='ORDER003'), (SELECT id FROM store_map WHERE code='STORE003'),  7, 5, '정말 만족해요!',     'rev3.jpg',  NOW(), NOW(), NULL),
    (gen_random_uuid(), (SELECT id FROM order_map WHERE code='ORDER004'), (SELECT id FROM store_map WHERE code='STORE004'),  4, 3, '보통이에요.',         'rev4.jpg',  NOW(), NOW(), NULL),
    (gen_random_uuid(), (SELECT id FROM order_map WHERE code='ORDER005'), (SELECT id FROM store_map WHERE code='STORE005'),  5, 4, '햄버거 좋아요.',     'rev5.jpg',  NOW(), NOW(), NULL),
    (gen_random_uuid(), (SELECT id FROM order_map WHERE code='ORDER006'), (SELECT id FROM store_map WHERE code='STORE006'),  6, 2, '짜장면이 별로였어요.','rev6.jpg', NOW(), NOW(), NULL),
    (gen_random_uuid(), (SELECT id FROM order_map WHERE code='ORDER007'), (SELECT id FROM store_map WHERE code='STORE007'),  7, 5, '탕수육 굿!',         'rev7.jpg',  NOW(), NOW(), NULL),
    (gen_random_uuid(), (SELECT id FROM order_map WHERE code='ORDER008'), (SELECT id FROM store_map WHERE code='STORE008'),  8, 5, '초밥 신선해요.',     'rev8.jpg',  NOW(), NOW(), NULL),
    (gen_random_uuid(), (SELECT id FROM order_map WHERE code='ORDER009'), (SELECT id FROM store_map WHERE code='STORE009'),  9, 4, '떡볶이 맛나요.',     'rev9.jpg',  NOW(), NOW(), NULL),
    (gen_random_uuid(), (SELECT id FROM order_map WHERE code='ORDER010'), (SELECT id FROM store_map WHERE code='STORE010'), 10, 3, '라면은 평범합니다.', 'rev10.jpg', NOW(), NOW(), NULL);

----------------------------
-- 13) 사장답글 (owner_reviews)
----------------------------
INSERT INTO owner_reviews (owner_review_id, review_id, user_no, owner_review_content, created_date, modified_date, deleted_date)
VALUES
    (gen_random_uuid(), (SELECT review_id FROM reviews r JOIN orders o ON r.order_id=o.order_id WHERE o.order_id=(SELECT id FROM order_map WHERE code='ORDER001')),  2, '리뷰 감사합니다!',          NOW(), NOW(), NULL),
    (gen_random_uuid(), (SELECT review_id FROM reviews r JOIN orders o ON r.order_id=o.order_id WHERE o.order_id=(SELECT id FROM order_map WHERE code='ORDER002')),  2, '다음에도 이용해주세요.',    NOW(), NOW(), NULL),
    (gen_random_uuid(), (SELECT review_id FROM reviews r JOIN orders o ON r.order_id=o.order_id WHERE o.order_id=(SELECT id FROM order_map WHERE code='ORDER003')), 11, '항상 최선을 다하겠습니다.', NOW(), NOW(), NULL),
    (gen_random_uuid(), (SELECT review_id FROM reviews r JOIN orders o ON r.order_id=o.order_id WHERE o.order_id=(SELECT id FROM order_map WHERE code='ORDER004')),  2, '소중한 의견 감사합니다.',   NOW(), NOW(), NULL),
    (gen_random_uuid(), (SELECT review_id FROM reviews r JOIN orders o ON r.order_id=o.order_id WHERE o.order_id=(SELECT id FROM order_map WHERE code='ORDER005')),  2, '좋은 하루 되세요!',        NOW(), NOW(), NULL),
    (gen_random_uuid(), (SELECT review_id FROM reviews r JOIN orders o ON r.order_id=o.order_id WHERE o.order_id=(SELECT id FROM order_map WHERE code='ORDER006')), 11, '더 노력하겠습니다.',        NOW(), NOW(), NULL),
    (gen_random_uuid(), (SELECT review_id FROM reviews r JOIN orders o ON r.order_id=o.order_id WHERE o.order_id=(SELECT id FROM order_map WHERE code='ORDER007')), 11, '리뷰 감사합니다!',          NOW(), NOW(), NULL),
    (gen_random_uuid(), (SELECT review_id FROM reviews r JOIN orders o ON r.order_id=o.order_id WHERE o.order_id=(SELECT id FROM order_map WHERE code='ORDER008')), 11, '만족하셨다니 기쁩니다.',    NOW(), NOW(), NULL),
    (gen_random_uuid(), (SELECT review_id FROM reviews r JOIN orders o ON r.order_id=o.order_id WHERE o.order_id=(SELECT id FROM order_map WHERE code='ORDER009')),  2, '리뷰 남겨주셔서 감사합니다.',NOW(), NOW(), NULL),
    (gen_random_uuid(), (SELECT review_id FROM reviews r JOIN orders o ON r.order_id=o.order_id WHERE o.order_id=(SELECT id FROM order_map WHERE code='ORDER010')),  2, '더 발전하는 매장이 되겠습니다.', NOW(), NOW(), NULL);

----------------------------
-- 14) 매장 이미지 (store_images)
----------------------------
INSERT INTO store_images (store_image_id, store_id, store_image_url, created_date, modified_date, deleted_date)
VALUES
    (gen_random_uuid(), (SELECT id FROM store_map WHERE code='STORE001'), 'store1.jpg',  NOW(), NOW(), NULL),
    (gen_random_uuid(), (SELECT id FROM store_map WHERE code='STORE002'), 'store2.jpg',  NOW(), NOW(), NULL),
    (gen_random_uuid(), (SELECT id FROM store_map WHERE code='STORE003'), 'store3.jpg',  NOW(), NOW(), NULL),
    (gen_random_uuid(), (SELECT id FROM store_map WHERE code='STORE004'), 'store4.jpg',  NOW(), NOW(), NULL),
    (gen_random_uuid(), (SELECT id FROM store_map WHERE code='STORE005'), 'store5.jpg',  NOW(), NOW(), NULL),
    (gen_random_uuid(), (SELECT id FROM store_map WHERE code='STORE006'), 'store6.jpg',  NOW(), NOW(), NULL),
    (gen_random_uuid(), (SELECT id FROM store_map WHERE code='STORE007'), 'store7.jpg',  NOW(), NOW(), NULL),
    (gen_random_uuid(), (SELECT id FROM store_map WHERE code='STORE008'), 'store8.jpg',  NOW(), NOW(), NULL),
    (gen_random_uuid(), (SELECT id FROM store_map WHERE code='STORE009'), 'store9.jpg',  NOW(), NOW(), NULL),
    (gen_random_uuid(), (SELECT id FROM store_map WHERE code='STORE010'), 'store10.jpg', NOW(), NOW(), NULL);


----------------------------
-- 15) 감사 컬럼 일괄 보정 (created_by / modified_by / deleted_by)
--     기존 INSERT 이후 실행되어도 무방
----------------------------

-- users: 자기 자신을 작성/수정자로 설정 (초기 시드 간편화)
UPDATE public.users u
SET created_by = u.user_no,
    modified_by = u.user_no
WHERE (created_by IS NULL OR modified_by IS NULL);

-- addresses: 소유자(user_no)를 작성/수정자로
UPDATE public.addresses a
SET created_by = a.user_no,
    modified_by = a.user_no
WHERE (created_by IS NULL OR modified_by IS NULL);

-- stores: 점주(user_no)를 작성/수정자로
UPDATE public.stores s
SET created_by = s.user_no,
    modified_by = s.user_no
WHERE (created_by IS NULL OR modified_by IS NULL);

-- products: 해당 상품의 매장 점주를 작성/수정자로
UPDATE public.products p
SET created_by = s.user_no,
    modified_by = s.user_no
    FROM public.stores s
WHERE p.store_id = s.store_id
  AND (p.created_by IS NULL OR p.modified_by IS NULL);

-- categories: 시스템 관리자(예: 1번 유저)를 작성/수정자로
-- 필요 시 관리자 user_no로 변경하세요.
UPDATE public.categories c
SET created_by = COALESCE(created_by, 1),
    modified_by = COALESCE(modified_by, 1)
WHERE (created_by IS NULL OR modified_by IS NULL);

-- category_stores: 매장 점주를 작성/수정자로
UPDATE public.category_stores cs
SET created_by = s.user_no,
    modified_by = s.user_no
    FROM public.stores s
WHERE cs.store_id = s.store_id
  AND (cs.created_by IS NULL OR cs.modified_by IS NULL);

-- category_products: 상품의 매장 점주를 작성/수정자로
UPDATE public.category_products cp
SET created_by = s.user_no,
    modified_by = s.user_no
    FROM public.products p
JOIN public.stores s ON p.store_id = s.store_id
WHERE cp.product_id = p.product_id
  AND (cp.created_by IS NULL OR cp.modified_by IS NULL);

-- orders: 주문자(user_no)를 작성/수정자로
UPDATE public.orders o
SET created_by = o.user_no,
    modified_by = o.user_no
WHERE (created_by IS NULL OR modified_by IS NULL);

-- carts: 주문자(user_no)를 작성/수정자로
UPDATE public.carts c
SET created_by = c.user_no,
    modified_by = c.user_no
WHERE (created_by IS NULL OR modified_by IS NULL);

-- deliveries: 배달 대상 주문의 사용자 또는 레코드의 user_no
UPDATE public.deliveries d
SET created_by = d.user_no,
    modified_by = d.user_no
WHERE (created_by IS NULL OR modified_by IS NULL);

-- reviews: 리뷰 작성자(user_no)
UPDATE public.reviews r
SET created_by = r.user_no,
    modified_by = r.user_no
WHERE (created_by IS NULL OR modified_by IS NULL);

-- owner_reviews: 사장(작성자) user_no
UPDATE public.owner_reviews orv
SET created_by = orv.user_no,
    modified_by = orv.user_no
WHERE (created_by IS NULL OR modified_by IS NULL);

-- store_images: 매장 점주를 작성/수정자로
UPDATE public.store_images si
SET created_by = s.user_no,
    modified_by = s.user_no
    FROM public.stores s
WHERE si.store_id = s.store_id
  AND (si.created_by IS NULL OR si.modified_by IS NULL);

-- deleted_by는 현재 목데이터가 삭제 상태(NULL) 가정이므로 유지
-- 필요 시 예: 소프트 삭제된 행에 대해 일괄 지정
-- UPDATE some_table SET deleted_by = 1 WHERE deleted_date IS NOT NULL AND deleted_by IS NULL;
