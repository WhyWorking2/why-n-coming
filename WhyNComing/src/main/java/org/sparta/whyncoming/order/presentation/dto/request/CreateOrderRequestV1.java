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

    @Schema(description = "장바구니 ID 목록", example = "[\"f96ee759-2f85-49a6-b72a-402b52ab4cd3\", \"f96ee759-2f85-49a6-b72a-402b52ab4cd4\"]")
    @NotNull(message = "장바구니 ID 리스트는 필수입니다.")
    @Size(min = 1, message = "최소 한 개 이상의 장바구니 ID가 필요합니다.")
    private List<UUID> cartIds;
}

