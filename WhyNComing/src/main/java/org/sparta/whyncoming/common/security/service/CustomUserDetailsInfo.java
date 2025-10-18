package org.sparta.whyncoming.common.security.service;

import org.sparta.whyncoming.user.domain.entity.User;
import org.sparta.whyncoming.user.domain.enums.UserRoleEnum;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

public class CustomUserDetailsInfo implements CustomUserDetails{

    private final User user;

    public CustomUserDetailsInfo(User user) {
        this.user = user;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public Integer getUserNo() {
        return user.getUserNo();
    }

    @Override
    public String getUserId() {
        return user.getUserId();
    }

    @Override
    public String getUserName() {
        return user.getUserName();
    }

    @Override
    public String getEmail() {
        return user.getEmail();
    }

    @Override
    public UserRoleEnum getRole() {
        return user.getRole();
    }

    @Override
    public Integer getAuthVersion() {return user.getAuthVersion();}



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // user.getRole()이 null이 아니면 "ROLE_"를 붙여서 반환
        UserRoleEnum role = user.getRole();
        if (role == null) {
            return List.of();
        }
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserName();
    }
}
