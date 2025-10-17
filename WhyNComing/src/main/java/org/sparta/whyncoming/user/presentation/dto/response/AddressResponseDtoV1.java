
package org.sparta.whyncoming.user.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.UUID;

@Getter
public class AddressResponseDtoV1 {

    @Schema(example = "6f1a4e60-8c6c-4a52-8b6d-1ad8c4d42a3f", description = "주소 UUID")
    private UUID addressId;

    @Schema(example = "서울특별시 강남구 테헤란로 123", description = "전체 주소")
    private String address;

    @Schema(example = "Y", description = "대표 주소 여부 (Y/N)")
    private String representativeYn;

    public AddressResponseDtoV1(UUID addressId, String address, String representativeYn) {
        this.addressId = addressId;
        this.address = address;
        this.representativeYn = representativeYn;
    }
}
