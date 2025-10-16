package org.sparta.whyncoming.common.security.service;


import org.sparta.whyncoming.user.domain.entity.User;
import org.sparta.whyncoming.user.domain.enums.UserRoleEnum;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * CustomUserDetails
 * - Spring Security의 UserDetails에 프로젝트 도메인 속성을 얹은 확장 인터페이스
 * - 컨트롤러/서비스 계층에서 캐스팅 없이 userNo, userId, email, role 등을 안전하게 조회하기 위함
 */
public interface CustomUserDetails extends UserDetails {

    User getUser();

    /** PK (users.user_no) */
    Integer getUserNo();

    /** 로그인 아이디 (users.user_id) */
    String getUserId();

    /** 표시 이름 (users.user_name) */
    String getUserName();

    /** 이메일 (users.email) */
    String getEmail();

    /** 역할 (users.role) */
    UserRoleEnum getRole();
}
