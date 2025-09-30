package org.sparta.whyncoming.common.response;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Hidden // 문서 스캔 제외
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 비즈니스 예외 -> 정의한 상태코드 & 메시지로 응답
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResult<Void>> handleBusiness(BusinessException ex) {
        return ResponseUtil.failure(ex.getErrorCode(), ex.getMessage());
    }

    // @Valid 바인딩 실패
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResult<Void>> handleValidation(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(err -> err.getField() + " " + err.getDefaultMessage())
                .orElse(ErrorCode.VALIDATION_FAILED.getMessage());
        return ResponseUtil.failure(ErrorCode.VALIDATION_FAILED, msg);
    }

    // @Validated on query/path 등에서 발생하는 제약 위반
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResult<Void>> handleConstraint(ConstraintViolationException ex) {
        String msg = ex.getConstraintViolations().stream()
                .findFirst()
                .map(v -> v.getPropertyPath() + " " + v.getMessage())
                .orElse(ErrorCode.VALIDATION_FAILED.getMessage());
        return ResponseUtil.failure(ErrorCode.VALIDATION_FAILED, msg);
    }

    // 그 외 모든 예외
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResult<Void>> handleEtc(Exception ex) {
        return ResponseUtil.exception(ex);
    }
}
