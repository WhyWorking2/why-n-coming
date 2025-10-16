package org.sparta.whyncoming.user.presentation.dto.response;

import lombok.Getter;

import org.sparta.whyncoming.user.domain.entity.User;

@Getter

public class UpdateUserResponseDtoV1 {
    private Integer userNo;
    private String userId;
    private String userName;
    private String userPhone;
    private String email;
    private String role;

    public UpdateUserResponseDtoV1(User u) {
        this.userNo = u.getUserNo();
        this.userId = u.getUserId();
        this.userName = u.getUserName();
        this.userPhone = u.getUserPhone();
        this.email = u.getEmail();
        this.role = u.getRole().name();
    }
}
