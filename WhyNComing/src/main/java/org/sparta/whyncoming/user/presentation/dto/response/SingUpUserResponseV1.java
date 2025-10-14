package org.sparta.whyncoming.user.presentation.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SingUpUserResponseV1 {

    private String UserId;
    private String UserName;

    public SingUpUserResponseV1(String UserId, String UserName) {
        this.UserId = UserId;
        this.UserName = UserName;
    }

    public static SingUpUserResponseV1 of(String UserId, String UserName) {     // 객체를 만들어낸다는 의미로 관용적으로 쓰이는 이름
        return new SingUpUserResponseV1(UserId, UserName);
    }
}
