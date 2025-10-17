package org.sparta.whyncoming.user.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Pattern;

/**
 * 배송지 수정(PATCH) 요청 DTO
 * - 모든 필드 선택 입력(부분수정)
 * - null이 아닌 필드만 서비스에서 반영
 */
public record AddressUpdateRequestDtoV1(

        @Schema(example = "서울특별시 강남구 테헤란로 123", description = "기본 주소")
        @Nullable
        String address,

        @Schema(example = "N", description = "대표 주소 여부: 'Y' 또는 'N'")
        @Pattern(regexp = "Y|N", message = "대표 주소 여부는 Y 또는 N 이어야 합니다.")
        @Nullable
        String representativeYn
) {}
