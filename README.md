# 🛍️ WhyNComing

> 배달의민족 기능을 모티브로 한 백엔드 전용 프로젝트  
> 실시간 배달원 기능 없이, 주문 → 수락 → 배달 시작 → 배달 완료까지의 상태 관리 중심으로 구현했습니다.

---

## 🚀 프로젝트 개요
- **프로젝트명**: Whyncoming Delivery
- **설명**: 입점사, 상품, 주문, 결제, 배달 상태를 관리하는 백엔드 시스템
- **개발 기간:** 2025.09.26 ~ 2025.10.20
- **참여 인원**: Backend 5명
- **주요 기능**: 회원 관리 / 주문 및 결제 / 카테고리 매핑 / 배달 상태 관리 / 이미지 업로드 / AI 기능

---

## ⚙️ 기술 스택
- **Language**: Java 17
- **Framework**: Spring Boot 3.x
- **Database**: PostgreSQL (Docker + Flyway 마이그레이션 관리)
- **Infra**: AWS / S3
- **Auth**: Spring Security + JWT
- **Cache**: Caffeine Cache (권한/버전/블랙리스트 캐싱)
- **Build Tool**: Gradle
- **AI**: OpenAI API 연동
- **Payment**: PG사 테스트 결제 연동 (토스페이)
- **Secret Management**: Infisical
- **API 문서화**: Swagger

---

## 📦 주요 기능
- **회원 관리 (User)**: JWT 기반 인증, 권한 관리
- **입점사 관리 (Seller)**: 입점사 등록 / 수정 / 삭제
- **상품 관리 (Product)**: 상품 등록 / 수정 / 삭제 / 조회
- **카테고리 관리 (Category)**: 카테고리 등록 / 삭제 / 조회 / 상품과 입점사 모두 M:N 관계, 매핑 테이블 관리
- **주문 및 결제 (Order/Payment)**: 주문 생성, 결제 연동, 상태 전이
- **배달 프로세스 (Delivery)**: 주문 수락 → 배달 시작 → 완료
- **장바구니 (Cart)**: 상품 추가 / 삭제 / 수량 변경
- **이미지 업로드 (Image)**: AWS S3 연동
- **AI 기능 (Assistant)**: 상품 추천 및 챗봇 기능 구현

---


## 📂 프로젝트 구조
```
src
┣ main/java/org.sparta.whyncoming
┃ ┣ common # 공통 유틸, 예외, 응답 포맷
┃ ┣ config # S3, JWT, Security 설정
┃ ┣ user # 회원 관련 로직
┃ ┣ store # 입점사 도메인
┃ ┣ product # 상품, 카테고리, AI 기능
┃ ┣ order # 주문, 장바구니, 결제, 배달, 리뷰
┃ ┗ test # 프로젝트 로직 예시
┗ resources
```
> MSA로 분리될 가능성을 염두에 두고 도메인을 적게 나누었습니다.

---

## 🧑‍💻 팀원 역할

| 이름            | GitHub                                       | 담당                                  | 주요 기여 |
|:--------------|:---------------------------------------------|:------------------------------------|:--|
| **한준희** (팀장)  | [@kingstree](https://github.com/kingstree)   | 유저 / 보안 / 발표 /                      | JWT + Security 인증 및 인가, 발표 진행 |
| **구대윤** (기술팀장)| [@kookong2](https://github.com/kookong2)     | 입점사 / ERD / 초기 설정                   | 초기 구조 설계 및 공통 환경 구성      |
| **박 결**       | [@parkg17](https://github.com/parkg17)       | 주문 / PG 결제 / Entity                 | 결제 연동 및 주문 상태 관리 , AI 기능 |
| **진주양**       | [@juyangjin](https://github.com/juyangjin)            | 상품 / 카테고리 / README                    | OpenAI 연동, 더미데이터 설계, 문서화 |
| **김소윤**       | [@soyxni](https://github.com/soyxni)         | 장바구니 / AWS S3                       | 이미지 업로드 기능, 발표자료 제작      |


> 일부 인원은 **AI / 문서 / 초기설정 등 중복 영역**에도 참여.

---
## 🧠 트러블슈팅

### 1️⃣ Infisical CLI 디버깅 불편
- **문제:** `infisical run -- ...` 실행 시 IDE 브레이크포인트/리런 불안정
- **해결:** **시크릿 yml 분리**. Infisical은 원천 저장소로만 사용, CI/로컬에서 `export → yml` 동기화

### 2️⃣ `pg` 에러: `FATAL: role "user" does not exist`
- **원인:** DB Role 미생성 또는 환경변수 불일치
- **해결:** 초기 SQL로 Role/DB 생성
  ```sql
  CREATE ROLE app_user WITH LOGIN PASSWORD 'app_password';
  CREATE DATABASE whyncoming OWNER app_user;
  GRANT ALL PRIVILEGES ON DATABASE whyncoming TO app_user;
  ```

### 3️⃣ `delete_by` 감사 컬럼 제거
- **이유:** `modified_by` 가 삭제자 역할 수행 → 중복 제거
- **효과:** 스키마 단순화, 쿼리 일관성 향상

### 4️⃣ `users` 테이블에서 `created_by`, `modified_by` 제거
- **문제:** 순환의존 / AuditorAware 초기화 문제
- **해결:** 별도 `user_audit_log` 테이블로 행위 로그 기록

### 5️⃣ `product` 조회 시 조인 후 dto타입 매핑으로 N+1 문제 해결
- **원인:** product 리스트 속 category 리스트를 조회하는 방식
- **해결:** JPQL로 조인 후 내부 리스트를 매핑하고 Dto 타입으로 반환하는 것으로 해결

---