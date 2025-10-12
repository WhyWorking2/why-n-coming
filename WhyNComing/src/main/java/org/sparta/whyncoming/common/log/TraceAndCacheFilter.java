package org.sparta.whyncoming.common.log;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.UUID;


public class TraceAndCacheFilter extends OncePerRequestFilter {

    public static final String TRACE_ID = "traceId";


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String u = request.getRequestURI();
        // Swagger/OpenAPI & 공통 제외 (정적/스트리밍 리소스는 절대 래핑하지 않음 → 500 예방)
        return u.startsWith("/api-docs")
                || u.startsWith("/swagger-ui")
                || u.equals("/swagger-ui.html")
                || u.startsWith("/swagger-resources")
                || u.startsWith("/webjars")
                || u.equals("/favicon.ico")
                || u.equals("/health")
                || u.startsWith("/actuator")
                || u.startsWith("/error"); // 스프링 기본 에러 엔드포인트
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        request.getRequestURI();

        // traceId: 클라이언트 제공값(X-Trace-Id) 우선, 없으면 생성
        String traceId = request.getHeader("X-Trace-Id");
        if (traceId == null || traceId.isBlank()) traceId = UUID.randomUUID().toString();
        MDC.put(TRACE_ID, traceId);

        try {
            if (shouldNotFilter(request)) {
                // 제외 경로는 래핑 없이 그대로 통과 (인터셉터에서도 제외하도록 설정)
                chain.doFilter(request, response);
                return;
            }

            // 일반 경로만 캐싱 래퍼 적용 (인터셉터가 바디 읽을 수 있게 함)
            ContentCachingRequestWrapper req = new ContentCachingRequestWrapper(request);
            ContentCachingResponseWrapper res = new ContentCachingResponseWrapper(response);
            try {
                chain.doFilter(req, res);
            } finally {
                // 반드시 마지막에 한 번만
                res.copyBodyToResponse();
            }
        } finally {
            MDC.remove(TRACE_ID);
        }
    }
}
