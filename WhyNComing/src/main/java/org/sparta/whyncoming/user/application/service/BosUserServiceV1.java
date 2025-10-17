package org.sparta.whyncoming.user.application.service;

import jakarta.transaction.Transactional;
import org.sparta.whyncoming.common.exception.BusinessException;
import org.sparta.whyncoming.common.exception.ErrorCode;
import org.sparta.whyncoming.user.domain.entity.User;
import org.sparta.whyncoming.user.domain.repository.UserRepository;
import org.sparta.whyncoming.user.presentation.dto.request.UpdateUserByAdminRequestDtoV1;

import org.sparta.whyncoming.user.presentation.dto.response.UpdateUserResponseDtoV1;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class BosUserServiceV1 {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public BosUserServiceV1(UserRepository userRepository,
                            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    /**
     * PATCH /member/{userId}
     */
    @Transactional
    public UpdateUserResponseDtoV1 updateUserInfo(Integer userNo, UpdateUserByAdminRequestDtoV1 request) {
        User userInfo = userRepository.findById(userNo).orElseThrow(
                () -> new BusinessException(ErrorCode.UNAUTHORIZED,"수정할 유저 정보가 없습니다.")
        );

        String newPassword = passwordEncoder.encode(request.getNewPassword().trim());

        // JPA Dirty Checking으로 업데이트: 새 엔티티 생성/저장은 금지
        if (request.getUserName() != null && !request.getUserName().isBlank()) {
            userInfo.updateName(request.getUserName().trim());
        }
        // 비밀번호
        if (request.getNewPassword() != null && !request.getNewPassword().isBlank()) {
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
        //전화번호
        if (request.getRole() != null) {
            userInfo.updateRole(request.getRole());
        }
        if(request.getDeleted()){
            userInfo.markDeleted();
        }

        User updateUser = userRepository.save(userInfo);

        return new UpdateUserResponseDtoV1(updateUser);

    }

}
