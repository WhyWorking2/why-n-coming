package org.sparta.whyncoming.common.exception;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sparta.whyncoming.common.log.util.StackTraceUtil;
import org.sparta.whyncoming.common.log.dto.ErrorLog;
import org.sparta.whyncoming.common.log.dto.LogType;
import org.sparta.whyncoming.common.response.ApiResult;
import org.sparta.whyncoming.common.response.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;

@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger ERR_LOG = LoggerFactory.getLogger("api.error");


    private void logError(int status, String customCode, String message, Exception ex) {
        ErrorLog e = new ErrorLog();
        e.setLogType(LogType.ERROR);
        e.setStatus(status);
        e.setMessage(message);
        e.setType(ex != null ? ex.getClass().getName() : null);
        e.setCustomCode(customCode);
        e.setStackTrace(StackTraceUtil.toTrimmedString(ex));
        try {
            ERR_LOG.error("{}", new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(e));
        } catch (Exception ignore) {
            ERR_LOG.error("[error-log] status={} code={} msg={}", status, customCode, message, ex);
        }
    }


    /**
     * 비즈니스 로직 예외 처리.
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResult<Void>> handleBusiness(BusinessException ex) {
        ErrorCode code = ex.getErrorCode();
        logError(code.getStatus().value(), code.name(), ex.getMessage(), ex);
        return ResponseUtil.failure(code, ex.getMessage());
    }


    /**
     * @Valid 검증 실패 처리.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResult<Void>> handleValidation(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(err -> err.getField() + " " + err.getDefaultMessage())
                .orElse(ErrorCode.VALIDATION_FAILED.getMessage());
        logError(ErrorCode.VALIDATION_FAILED.getStatus().value(), ErrorCode.VALIDATION_FAILED.name(), msg, ex);
        return ResponseUtil.failure(ErrorCode.VALIDATION_FAILED, msg);
    }


    /**
     * @Validated 제약 조건 위반 처리.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResult<Void>> handleConstraint(ConstraintViolationException ex) {
        String msg = ex.getConstraintViolations().stream()
                .findFirst()
                .map(v -> v.getPropertyPath() + " " + v.getMessage())
                .orElse(ErrorCode.VALIDATION_FAILED.getMessage());
        logError(ErrorCode.VALIDATION_FAILED.getStatus().value(), ErrorCode.VALIDATION_FAILED.name(), msg, ex);
        return ResponseUtil.failure(ErrorCode.VALIDATION_FAILED, msg);
    }


    /**
     * 인증 또는 인가 거부 시 403 응답 처리.
     */
    @ExceptionHandler({ AuthorizationDeniedException.class, AccessDeniedException.class })
    public ResponseEntity<ApiResult<Void>> handleAccessDenied(RuntimeException ex) {
        logError(HttpStatus.FORBIDDEN.value(), ErrorCode.FORBIDDEN.name(), "Access Denied", (Exception) ex);
        return ResponseUtil.failure(ErrorCode.FORBIDDEN, "Access Denied");
    }


    /**
     * 기타 모든 예외 처리 (500 응답).
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResult<Void>> handleEtc(Exception ex) {
        logError(HttpStatus.INTERNAL_SERVER_ERROR.value(), ErrorCode.INTERNAL_SERVER_ERROR.name(), ex.getMessage(), ex);
        return ResponseUtil.exception(ex);
    }
}
