package org.sparta.whyncoming.order.presentation.dto.reqeust;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sparta.whyncoming.order.domain.entity.Cart;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Schema(description = "결제 요청 DTO")
public class CreatePaymentRequestV1 {

    @Schema(description = "유저 ID", example = "101")
    @NotNull(message = "유저 ID는 필수입니다.")
    private Integer userId;

    @Schema(description = "가게 ID", example = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
    @NotNull(message = "가게 ID는 필수입니다.")
    private UUID storeId;

    @Schema(description = "장바구니 아이템 목록")
    @NotNull(message = "주문 항목 리스트는 필수입니다.")
    @Size(min = 1, message = "주문 항목은 최소 한 개 이상이어야 합니다.")
    private List<Cart> items;

    @Schema(description = "결제 수단", example = "CARD")
    @NotBlank(message = "결제 수단은 필수입니다.")
    private String paymentMethod;

    @Schema(description = "요청사항", example = "소스 빼주세요")
    private String requests;

    @Schema(description = "배달 주소", example = "서울시 강남구 테헤란로 123")
    @NotBlank(message = "주소는 필수입니다.")
    private String address;

    @Schema(description = "연락처", example = "010-1234-5678")
    @NotBlank(message = "연락처는 필수입니다.")
    private String contactNumber;
}
