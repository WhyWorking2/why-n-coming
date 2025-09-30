package org.sparta.whyncoming.test.presentaion.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateTestRequestV1 {

    @Schema(description = "수정할 내용", example = "업데이트된 내용")
    @NotBlank(message = "name은 필수입니다.")
    private String name;

    public UpdateTestRequestV1(String name) {
        this.name = name;
    }
}
