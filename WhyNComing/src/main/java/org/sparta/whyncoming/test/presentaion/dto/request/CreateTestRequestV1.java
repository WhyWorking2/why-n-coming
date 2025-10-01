package org.sparta.whyncoming.test.presentaion.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateTestRequestV1 {

    @Schema(description = "내용", example = "블라블라")
    @NotBlank(message = "name은 필수입니다.")
    private String name;
}
