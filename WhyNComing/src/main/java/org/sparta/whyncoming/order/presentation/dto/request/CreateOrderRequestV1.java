package org.sparta.whyncoming.order.presentation.dto.request;

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
@Schema(description = "주문 요청 DTO")
public class CreateOrderRequestV1 {

    @Schema(description = "회원번호", example = "101")
    @NotBlank(message = "회원번호는 필수입니다.")
    private Integer userNo;

    @Schema(description = "가게ID", example = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
    @NotBlank(message = "가게ID는 필수입니다.")
    private UUID storeId;

    @Schema(description = "장바구니 아이템 목록")
    @NotNull(message = "주문 항목 리스트는 필수입니다.")
    @Size(min = 1, message = "주문 항목은 최소 한 개 이상이어야 합니다.")
    private List<Cart> items;
}

