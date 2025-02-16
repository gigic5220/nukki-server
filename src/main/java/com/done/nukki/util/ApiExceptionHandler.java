package com.done.nukki.util;

import com.done.nukki.dto.ApiResponseDto;
import com.done.nukki.exception.NotFoundException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackages = "com.done.nukki.controller")
public class ApiExceptionHandler {

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiResponseDto<Object>> handleExpiredJwtException(ExpiredJwtException exception) {
        ApiResponseDto<Object> errorResponse = ApiResponseDto.error(HttpStatus.UNAUTHORIZED.value(), "토큰 만료");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponseDto<Object>> handleMemberNotFoundException(NotFoundException exception) {
        ApiResponseDto<Object> errorResponse = ApiResponseDto.error(HttpStatus.NOT_FOUND.value(), "사용자 없음");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiResponseDto<Object>> handleJwtException(JwtException ex) {
        ApiResponseDto<Object> errorResponse = ApiResponseDto.error(HttpStatus.UNAUTHORIZED.value(), "토큰 오류");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDto<Object>> handleException(Exception ex) {
        // 기본 에러 응답 생성
        ApiResponseDto<Object> errorResponse = ApiResponseDto.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}