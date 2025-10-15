package org.sparta.whyncoming.common.security.service;


import org.sparta.whyncoming.user.domain.entity.User;
import org.sparta.whyncoming.user.domain.enums.UserRoleEnum;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;

public class UserDetailsImpl implements CustomUserDetails {

    private final User user;

    public UserDetailsImpl(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() { return user.getUserName(); }

    public String getUserId() {
        return user.getUserId();
    }

    @Override
    public String getUserName() {
        return user.getUserName();
    }

    public Integer getUserNo() {
        return user.getUserNo();
    }

    public UserRoleEnum getRole() {
        return user.getRole();
    }

    public String getEmail() {
        return user.getEmail();
    }
    public String getPhone() {
        return user.getUserPhone();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        UserRoleEnum role = user.getRole();
        String authority = role.getAuthority();

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(simpleGrantedAuthority);

        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
