package org.sparta.whyncoming.user.application.service;

import jakarta.transaction.Transactional;
import org.sparta.whyncoming.common.encode.EncoderUtil;


import org.sparta.whyncoming.common.exception.BusinessException;
import org.sparta.whyncoming.common.exception.ErrorCode;
import org.sparta.whyncoming.common.security.service.CustomUserDetails;
import org.sparta.whyncoming.common.security.service.CustomUserDetailsInfo;
import org.sparta.whyncoming.common.security.service.CustomUserDetailsService;

import org.sparta.whyncoming.common.security.jwt.JwtUtil;
import org.sparta.whyncoming.user.domain.entity.User;
import org.sparta.whyncoming.user.domain.enums.UserRoleEnum;
import org.sparta.whyncoming.user.domain.repository.UserRepository;

import org.sparta.whyncoming.user.presentation.dto.request.SignUpUserRequestDtoV1;
import org.sparta.whyncoming.user.presentation.dto.request.UpdateUserRequestDtoV1;

import org.sparta.whyncoming.user.presentation.dto.response.SingUpUserResponseDtoV1;
import org.sparta.whyncoming.user.presentation.dto.response.UpdateUserResponseDtoV1;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;


@Service
public class UserServiceV1 {

    private final UserRepository userRepository;
    private final EncoderUtil encoderUtil;
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService customUserDetailsService;

    public UserServiceV1(UserRepository userRepository,
                         EncoderUtil encoderUtil,
                         JwtUtil jwtUtil,
                         PasswordEncoder passwordEncoder,
                         CustomUserDetailsService customUserDetailsService) {
        this.userRepository = userRepository;
        this.encoderUtil = encoderUtil;
        this.passwordEncoder = passwordEncoder;
        this.customUserDetailsService = customUserDetailsService;
    }

    public String health() {
        return "ok";
    }

    // ADMIN_TOKEN

    @Transactional
    public SingUpUserResponseDtoV1 signup(SignUpUserRequestDtoV1 requestDto) {
        // Extract fields
        String userId = requestDto.getUserId();      // 로그인용 ID
        String userName = requestDto.getUserName();  // 사용자 이름
        String userPhone = requestDto.getUserPhone();// 휴대폰 번호
        String email = requestDto.getEmail();
        String password = encoderUtil.encode(requestDto.getPassword());

        // 중복 체크: userId, email
        userRepository.findByUserId(userId).ifPresent(u -> {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "중복된 사용자 ID가 존재합니다.");
        });
        userRepository.findByEmail(email).ifPresent(u -> {
            throw new BusinessException(ErrorCode.UNAUTHORIZED,"중복된 Email 입니다.");
        });

        // ROLE 설정 (관리자 토큰 확인 시 ADMIN 부여)
        UserRoleEnum role = UserRoleEnum.CUSTOMER;
        if (requestDto.getRole().equals(UserRoleEnum.MASTER) || requestDto.getRole().equals(UserRoleEnum.MANAGER)) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new BusinessException(ErrorCode.UNAUTHORIZED," 잘못된 어드민 토큰입니다.");
            }
            role = requestDto.getRole();
        }

        // 사용자 생성 및 저장
        User user = new User(userId, password, userName, userPhone, email, role);
        user = userRepository.save(user);
        return toResponse(user);
    }
    private SingUpUserResponseDtoV1 toResponse(User e) {
        return new SingUpUserResponseDtoV1(e.getUserId(), e.getUserName());
    }


    /**
     * PATCH /member/{userId}
     */
    @Transactional
    public UpdateUserResponseDtoV1 updateSelf(CustomUserDetailsInfo user, UpdateUserRequestDtoV1 request) {
        User userInfo = userRepository.findById(user.getUserNo()).orElseThrow(
                () -> new BusinessException(ErrorCode.UNAUTHORIZED,"수정할 유저 정보가 없습니다.")
        );
        if (userInfo.isDeleted()) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED," 탈퇴한 회원입니다.");
        }
        String newPassword = passwordEncoder.encode(request.getNewPassword().trim());

        // JPA Dirty Checking으로 업데이트: 새 엔티티 생성/저장은 금지
        if (request.getUserName() != null && !request.getUserName().isBlank()) {
            userInfo.updateName(request.getUserName().trim());
        }
        // 비밀번호
        if (passwordEncoder.encode(request.getOldPassword()).equals(user.getPassword()) && request.getNewPassword() != null && !request.getNewPassword().isBlank()) {
            userInfo.updatePassword(newPassword);
        }
        // 이메일
        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            userInfo.updateEmail(request.getEmail().trim());
        }
        //전화번호
        if (request.getUserName() != null && !request.getUserName().isBlank()) {
            userInfo.updatePhone(request.getUserPhone().trim());
        }

        User updateUser = userRepository.save(userInfo);

        /*
        정보수정에 오류가 발생하여 기존 SpringSecurity에 저장된 SecurityContext 또한 수정을 해야한다는 것을 알게되어
        기존 정보 호출
         */
        CustomUserDetails updatedUserDetails = customUserDetailsService.loadUserByUserNo(updateUser.getUserNo());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        /*
        호출된 값을 setAuthentication를 사용하여 새 값으로 변경하고 최종 setContext하여 업데이트 처리
         */

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(updateAuthentication(authentication, updatedUserDetails));
        SecurityContextHolder.setContext(context);

        return new UpdateUserResponseDtoV1(updateUser);

    }

    /*
 업데이트 인증처리에 대한 추가적인 메서드를 작성하여 기존 인증값과 현재 업데이트 된 값을 가져와서 업데이트 처리
  */
    protected Authentication updateAuthentication(Authentication authentication, CustomUserDetails userDetails) {
        UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(
                userDetails, authentication.getCredentials(), userDetails.getAuthorities());
        newAuth.setDetails(authentication.getDetails());

        return newAuth;
    }

    /**
     * DELETE /User/{userId}
     */
    @Transactional
    public void deleteSelf(CustomUserDetailsInfo user) {
        int updated = userRepository.softDeleteByUserNo(user.getUserNo(), Instant.now());
        if (updated == 0) {
            // 이미 탈퇴했거나 존재하지 않는 경우
            throw new BusinessException(ErrorCode.NOT_FOUND, "이미 탈퇴했거나 존재하지 않는 회원입니다.");
        }
    }

}
