package org.sparta.whyncoming.common.security.service;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface CustomUserDetailsService extends UserDetailsService {
    CustomUserDetails loadUserByUserNo(Integer userNo);
}
