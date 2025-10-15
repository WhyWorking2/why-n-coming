package org.sparta.whyncoming.common.security.service;


import org.sparta.whyncoming.user.domain.entity.User;
import org.sparta.whyncoming.user.domain.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements CustomUserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public CustomUserDetailsInfo loadUserByUserNo(Integer userNo) throws UsernameNotFoundException {
        User user = userRepository.findByUserNo(userNo)
                .orElseThrow(() -> new UsernameNotFoundException("Not Found " + userNo));

        return new CustomUserDetailsInfo(user);
    }

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserId(username)
                .orElseThrow(() -> new UsernameNotFoundException("Not Found " + username));

        return new CustomUserDetailsInfo(user);
    }
}
