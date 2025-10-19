# 🛍️ WhyNComing

쇼핑·예약 등 도메인을 대상으로 **JWT 기반 인증/인가**, **Flyway 마이그레이션**, **Caffeine 캐시 최적화**를 적용한 Spring Boot 서비스입니다.

---

## ✨ 핵심 기능

- **사용자 인증(Authentication)**: Access/Refresh **JWT** 발급, 회전(Rotation), 로그아웃 블랙리스트
- **인가(Authorization)**: `@PreAuthorize` / `@PostAuthorize`, **RoleHierarchy**(`MASTER > ADMIN > OWNER > CUSTOMER`), 리소스 소유권 `Policy` 검증
- **토큰 무효화 & 권한 캐싱**
    - `AuthVersionCache`: `userNo → auth_version` (불일치 시 **즉시 401**)
    - `TokenBlacklistCache`: 로그아웃/재발급 시 `jti` 차단
    - `TokenAuthorityCache`: `jti → GrantedAuthorities` 캐시로 DB 조회 최소화
- **데이터 관리**: **Flyway**로 스키마 버전관리, **Soft Delete**(`deleted_date`) 표준화
- **운영 편의**: Spring **Actuator** 헬스 엔드포인트 분리, 표준 에러 포맷/로깅 구조화

---

## 🧰 기술 스택

| 구분 | 기술 |
|------|------|
| **Backend** | Spring Boot 3.5.6, Spring Security, JPA, Validation, AOP, Actuator |
| **Database** | PostgreSQL, Flyway |
| **Cache** | Caffeine Cache (권한/버전/블랙리스트 캐싱) |
| **Build / Repo** | Gradle, GitHub |
| **API 문서** | Swagger (OpenAPI 3.0) |
| **Secrets 관리** | Infisical (중앙 저장) → 정적 yml 로딩 전략 |
| **Infra / DevOps** | Docker Compose, CI/CD 확장 구조 |

---

## 🗂️ 디렉토리 구조 (예시)

```
src
 └─ main
    ├─ java/com/whyn/...
    │   ├─ auth/ (JWT, 필터, 토큰 유틸, 리프레시)
    │   ├─ security/ (SecurityFilterChain, RoleHierarchy, Policy)
    │   ├─ cache/ (TokenCaches: Authority / Blacklist / AuthVersion)
    │   ├─ audit/ (AuditorAware, BaseTimeEntity)
    │   ├─ domain/ (엔티티, 리포지토리)
    │   └─ api/ (Controller, DTO)
    └─ resources/
        ├─ application.yml
        └─ db/migration/ (Flyway SQL 스크립트)
```

---

## 🚀 빠른 시작

### 1️⃣ 시크릿 설정 (yml)
`application-secret.yml` 파일을 환경별로 분리해 관리합니다.  
Infisical은 원천 저장소로만 사용하며, 실행은 정적 yml 로딩 방식으로 전환했습니다.

```yaml
whyn:
  auth:
    jwt:
      secret: "change-me"
      refreshSecret: "change-me"

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/whyncoming
    username: app_user
    password: app_password
```

> 실행 시:
> ```bash
> SPRING_PROFILES_ACTIVE=dev,secret
> SPRING_CONFIG_ADDITIONAL_LOCATION=file:/opt/app/secret/
> ```

---

### 2️⃣ Docker (선택)

```bash
docker compose up -d
```

---

### 3️⃣ 애플리케이션 실행

```bash
./gradlew bootRun
# 또는 IDE에서 Run/Debug
```

---

### 4️⃣ Swagger UI

```
http://localhost:8080/swagger-ui/index.html
```

---

## 🔐 인증 / 인가 요약

### ✅ 인증 (Authentication)

- `POST /v1/auth/login` → 자격 검증(BCrypt) → Access/Refresh JWT 발급

**JWT Claims 예시**
```json
{
  "userNo": 5,
  "userId": "user05",
  "role": "CUSTOMER",
  "authVersion": 1
}
```

---

### ✅ 인가 (Authorization)

- **권한 계층**
    - `MASTER > ADMIN > OWNER > CUSTOMER`
- **도메인 접근 제어**
    - `@PreAuthorize("@policy.isOwner(#orderId)")` 로 사용자 리소스 검증
- **스코프/클레임 기반 인가**
    - JWT `roles`, `auth_version` 활용

---

## 🧱 데이터 & 마이그레이션

- **Flyway** 자동 마이그레이션 적용
- **Soft Delete**: `deleted_date` 로 삭제 상태 표현 (`delete_by` 제거)
- **Auditing**
    - `created_date`, `modified_date` 유지
    - `users` 테이블은 `created_by`, `modified_by` 제거 (순환의존 해결)
    - 행위 감사는 `user_audit_log(actor_user_id, target_user_id, action, occurred_at)` 별도 테이블 기록

---

## ⚡ 캐싱 전략 (Caffeine)

| 캐시 이름 | 키 | 값 | 목적 |
|------------|----|----|------|
| `TokenAuthorityCache` | jti | GrantedAuthorities | 토큰 권한 조회 속도 향상 |
| `TokenBlacklistCache` | jti | 만료시각 | 로그아웃/재발급 시 즉시 차단 |
| `AuthVersionCache` | userNo | auth_version | 토큰 버전 불일치 시 즉시 401 |

---

## 🧩 트러블슈팅 기록

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

---


> 본 문서는 WhyNComing 프로젝트의 기술 구조 및 트러블슈팅 이력을 기반으로 작성되었습니다.  
> 실제 운영 환경의 포트, 계정, TTL 값은 팀 표준에 맞게 조정하세요.
