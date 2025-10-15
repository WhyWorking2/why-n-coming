package org.sparta.whyncoming.user.presentation.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;


@Getter
public class UpdateUserRequestDtoV1{

    @Schema(description = "표시 이름", example = "김개발")
    @Size(min = 1, max = 50, message = "이름은 1~50자여야 합니다.")
    @NotBlank
    private final String userName;

    @Schema(description = "유저 아이디", example = "testId")
    @Size(min = 1, max = 50, message = "아이디는 n 자 입니다.")
    @NotBlank
    private final String userId;


    @Schema(description = "휴대폰 번호(숫자만 10~11자리)", example = "01012345678")
    @Pattern(regexp = "^\\d{10,11}$", message = "전화번호는 숫자 10~11자리여야 합니다.")
    @NotBlank
    private final String userPhone;

    @Schema(description = "이메일", example = "dev@example.com")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    @Size(max = 100, message = "이메일은 최대 100자입니다.")
    @NotBlank
    private final String email;

    @Schema(description = "기존 패스워드", example = "testOldPass!")
    @NotBlank
    private final String oldPassword;

    @Schema(description = "신규 패스워드", example = "testNewPass!")
    @NotBlank
    private final String newPassword;


    public UpdateUserRequestDtoV1(String userName, String userId,String userPhone, String email, String oldPassword, String newPassword) {
        this.userName = userName;
        this.userId = userId;
        this.userPhone = userPhone;
        this.email = email;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }
}
