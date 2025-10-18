package org.sparta.whyncoming.common.security;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.sparta.whyncoming.common.security.jwt.JwtUtil;
import org.sparta.whyncoming.user.domain.enums.UserRoleEnum;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    private final JwtUtil jwtUtil;

    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Operation(
            summary = "로그인 (JWT 발급)",
            description = "userId / password로 로그인 후 JWT를 반환합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "로그인 성공",
                            content = @Content(schema = @Schema(implementation = LoginResponse.class))),
                    @ApiResponse(responseCode = "401", description = "인증 실패")
            }
    )
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request, HttpServletResponse LoginResponse) {

        String token = jwtUtil.createToken(request.userNo(), request.userId, request.role, request.authVersion);
        LoginResponse.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);
        return ResponseEntity.ok(new LoginResponse(token));
    }

    // === DTOs ===
    public record LoginRequest(
            @Schema(example = "1") Integer userNo,
            @Schema(example = "user01") String userId,
            @Schema(example = "MANAGER") UserRoleEnum role,
            @Schema(example = "1") Integer authVersion

    ) {}

    public record LoginResponse(
            @Schema(example = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...") String authorization
    ) {}
}
