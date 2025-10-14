package org.sparta.whyncoming.user.application.service;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.sparta.whyncoming.common.encode.EncoderUtil;

import org.sparta.whyncoming.common.security.jwt.JwtUtil;
import org.sparta.whyncoming.user.domain.entity.User;
import org.sparta.whyncoming.user.domain.enums.UserRoleEnum;
import org.sparta.whyncoming.user.domain.repository.UserRepository;

import org.sparta.whyncoming.user.presentation.dto.request.LoginRequestDto;
import org.sparta.whyncoming.user.presentation.dto.request.SignUpUserRequestDtoV1;
import org.sparta.whyncoming.user.presentation.dto.response.SingUpUserResponseV1;
import org.springframework.stereotype.Service;


@Service
public class UserServiceV1 {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final EncoderUtil encoderUtil;
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    public UserServiceV1(UserRepository userRepository, EncoderUtil encoderUtil,JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.encoderUtil = encoderUtil;
        this.jwtUtil = jwtUtil;
    }

    public String health() {
        return "ok";
    }

    // ADMIN_TOKEN

    @Transactional
    public SingUpUserResponseV1 signup(SignUpUserRequestDtoV1 requestDto) {
        // Extract fields
        String userId = requestDto.getUserId();      // 로그인용 ID
        String userName = requestDto.getUserName();  // 사용자 이름
        String userPhone = requestDto.getUserPhone();// 휴대폰 번호
        String email = requestDto.getEmail();
        String password = encoderUtil.encode(requestDto.getPassword());

        // 중복 체크: userId, email
        userRepository.findByUserId(userId).ifPresent(u -> {
            throw new IllegalArgumentException("중복된 사용자 ID가 존재합니다.");
        });
        userRepository.findByEmail(email).ifPresent(u -> {
            throw new IllegalArgumentException("중복된 Email 입니다.");
        });

        // ROLE 설정 (관리자 토큰 확인 시 ADMIN 부여)
        UserRoleEnum role = UserRoleEnum.CUSTOMER;
        if (requestDto.getRole().equals(UserRoleEnum.MASTER) || requestDto.getRole().equals(UserRoleEnum.MANAGER)) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = requestDto.getRole();
        }

        // 사용자 생성 및 저장
        User user = new User(userId, password, userName, userPhone, email, role);
        user = userRepository.save(user);
        return toResponse(user);
    }
    private SingUpUserResponseV1 toResponse(User e) {
        return new SingUpUserResponseV1(e.getUserId(), e.getUserName());
    }
/*
    public void login(LoginRequestDto requestDto, HttpServletResponse res) {
        String userId = requestDto.getUserId();
        String password = requestDto.getPassword();

        // 사용자 확인
        User user = userRepository.findByUserId(userId).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
        );

        // 비밀번호 확인
        if (!encoderUtil.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // JWT 생성 및 쿠키에 저장 후 Response 객체에 추가
        String token = jwtUtil.createToken(user.getUserNo(), user.getUserId(), user.getRole());
        //jwtUtil.addJwtToCookie(token, res);
    }*/
}
