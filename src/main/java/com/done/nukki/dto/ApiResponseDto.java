package com.done.nukki.dto;

import lombok.Getter;

// 공통 응답 DTO
@Getter
public class ApiResponseDto<T> {
    private final boolean success;
    private final int statusCode;
    private final String message;
    private final T data;

    // 생성자 (private, 정적 메서드를 통해 생성)
    private ApiResponseDto(boolean success, int statusCode, String message, T data) {
        this.success = success;
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

    // 성공 응답 생성 메서드
    public static <T> ApiResponseDto<T> success(T data) {
        return new ApiResponseDto<>(true, 200, "success", data);
    }

    // 성공 응답 생성 메서드
    public static <T> ApiResponseDto<T> deleteSuccess() {
        return new ApiResponseDto<>(true, 204, "success", null);
    }

    // 실패 응답 생성 메서드
    public static <T> ApiResponseDto<T> error(int statusCode, String message) {
        return new ApiResponseDto<>(false, statusCode, message, null);
    }
}
