package com.done.nukki.util;

import com.done.nukki.dto.ApiResponseDto;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackages = "com.done.nukki.controller")
public class ApiExceptionHandler {

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiResponseDto<Object>> handleExpiredJwtException(ExpiredJwtException ex) {
        ApiResponseDto<Object> errorResponse = ApiResponseDto.error(HttpStatus.UNAUTHORIZED.value(), "Refresh token expired");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiResponseDto<Object>> handleJwtException(JwtException ex) {
        ApiResponseDto<Object> errorResponse = ApiResponseDto.error(HttpStatus.UNAUTHORIZED.value(), "Invalid refresh token");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDto<Object>> handleException(Exception ex) {
        // 기본 에러 응답 생성
        ApiResponseDto<Object> errorResponse = ApiResponseDto.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}