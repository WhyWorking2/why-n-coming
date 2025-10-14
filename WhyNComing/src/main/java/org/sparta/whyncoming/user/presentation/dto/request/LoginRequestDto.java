package org.sparta.whyncoming.user.presentation.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
public class LoginRequestDto {
    private final String userId;
    private final String password;

    public LoginRequestDto(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }
}
