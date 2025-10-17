package org.sparta.whyncoming.user.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;


@Getter
public class AddressCreateRequestDtoV1 {

    @NotBlank
    @Schema(example = "서울특별시 강남구 테헤란로 123", description = "전체 주소")
    private String address;

    @Schema(example = "Y", description = "대표 주소 여부 (Y/N). 기본값은 N")
    private String representativeYn = "N";

    public AddressCreateRequestDtoV1(String address, String representativeYn) {
        this.address = address;
        this.representativeYn = representativeYn;
    }
}
