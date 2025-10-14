package org.sparta.whyncoming.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;

@Configuration //이 클래스가 스프링 설정을 위한 클래스임을 나타낸다.
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class DataConfig { // 지속성 엔티티에 대한 감사 활성화

    @Bean
    AuditorAware<String> auditorAware() { // 감사 목적으로 인증된 사용자의 userId를 반환
        return () -> Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(auth -> auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken))
                .map(auth -> {
                    Object principal = auth.getPrincipal();

                    // 1) Custom UserDetails 구현체에 userNo가 있을 때 (예: CustomUserPrincipal#getUserNo)
                    try {
                        Class<?> clazz = principal.getClass();
                        // 리플렉션으로 getUserNo()가 있으면 우선 사용
                        var method = clazz.getMethod("getUserNo");
                        Object val = method.invoke(principal);
                        if (val != null) return String.valueOf(val);
                    } catch (Exception ignore) { /* no-op */ }

                    // 2) JWT 기반 (Resource Server 등): 클레임에서 userNo 사용 (없으면 numeric sub 허용)
                    if (principal instanceof Jwt jwt) {
                        String uno = jwt.getClaimAsString("userNo");
                        if (uno == null) uno = jwt.getClaimAsString("user_no");
                        if (uno == null) {
                            String sub = jwt.getClaimAsString("sub");
                            if (sub != null && sub.matches("\\d+")) return sub; // sub가 숫자면 userNo로 간주
                        }
                        return uno;
                    }

                    // 3) 일반 UserDetails라면 getUsername()이 userId라면 그대로 사용
                    if (principal instanceof UserDetails ud) {
                        return ud.getUsername();
                    }

                    // 4) 마지막으로 Authentication 이름 사용 (환경에 따라 userId일 수 있음)
                    return auth.getName();
                });
    }
}
