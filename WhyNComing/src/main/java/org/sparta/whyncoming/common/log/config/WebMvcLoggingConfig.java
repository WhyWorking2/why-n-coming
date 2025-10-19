package org.sparta.whyncoming.common.log.config;

import org.sparta.whyncoming.common.log.ApiLoggingInterceptor;
import org.sparta.whyncoming.common.log.TraceAndCacheFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebMvcLoggingConfig는 Spring MVC 요청/응답 로깅을 위한 설정 클래스입니다.
 * - ApiLoggingInterceptor: 모든 API 요청과 응답을 로깅하도록 등록합니다.
 * - TraceAndCacheFilter: 요청에 traceId를 부여하고 캐싱 로깅 필터를 등록합니다.
 * Swagger, actuator, 정적 리소스 등의 경로는 로깅 대상에서 제외됩니다.
 */
@Configuration
public class WebMvcLoggingConfig implements WebMvcConfigurer {

    private final ApiLoggingInterceptor interceptor;

    public WebMvcLoggingConfig(ApiLoggingInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor)
                .addPathPatterns("/**")
                // 필터와 동일한 경로는 인터셉터도 제외
                .excludePathPatterns(
                        "/", "/api-docs/**", "/api-docs",
                        "/v3/api-docs/**", "/v3/api-docs",
                        "/swagger-ui/**", "/swagger-ui.html",
                        "/swagger-resources/**",
                        "/webjars/**",
                        "/favicon.ico",
                        "/health",
                        "/actuator/**",
                        "/error/**", "/error"
                );
    }

    @Bean
    public FilterRegistrationBean<TraceAndCacheFilter> traceAndCacheFilter() {
        FilterRegistrationBean<TraceAndCacheFilter> reg = new FilterRegistrationBean<>();
        reg.setFilter(new TraceAndCacheFilter());
        reg.addUrlPatterns("/*");
        reg.setOrder(1); // 보통 보안/트레이싱 필터 뒤, Spring MVC 전 단계
        return reg;
    }
}
