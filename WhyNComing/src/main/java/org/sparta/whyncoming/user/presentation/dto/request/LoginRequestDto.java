package org.sparta.whyncoming.user.presentation.dto.request;

import lombok.Getter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
public class LoginRequestDto {
    private final String userId;
    private final String password;

    @JsonCreator
    public LoginRequestDto(
            @JsonProperty("userId") String userId,
            @JsonProperty("password") String password) {
        this.userId = userId;
        this.password = password;
    }
}
