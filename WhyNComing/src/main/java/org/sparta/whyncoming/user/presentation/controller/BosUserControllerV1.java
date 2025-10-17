package org.sparta.whyncoming.user.presentation.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.sparta.whyncoming.user.application.service.UserServiceV1;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/bos/user")
@Tag(name = "BosUser", description = "bos용 User 데이터 API")
public class BosUserControllerV1 {

    private final UserServiceV1 userServiceV1;
    private final PasswordEncoder passwordEncoder;

    public BosUserControllerV1(UserServiceV1 userServiceV1, PasswordEncoder passwordEncoder) {
        this.userServiceV1 = userServiceV1;
        this.passwordEncoder = passwordEncoder;
    }




}
