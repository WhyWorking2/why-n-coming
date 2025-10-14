package org.sparta.whyncoming.user.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.sparta.whyncoming.user.domain.enums.UserRoleEnum;

@Getter
@Setter
public class SignUpUserRequestDtoV1 {

    @Schema(description = "userId", example = "testId")
    @NotBlank
    private String userId;
    @Schema(description = "userPw", example = "1234")
    @NotBlank
    private String password;
    @Schema(description = "유저 명", example = "테스터1")
    @NotBlank
    private String userName;
    @Schema(description = "userPhone", example = "01012341234")
    private String userPhone;
    @Schema(description = "userEmail", example = "email@email.com")
    @Email
    @NotBlank
    private String email;
    @Schema(description = "권한", example = "CUSTOMER")
    private UserRoleEnum role = UserRoleEnum.CUSTOMER;

    private String adminToken = "";



}
