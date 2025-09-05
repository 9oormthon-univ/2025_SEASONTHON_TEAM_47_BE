package com._oormthon.goEuro.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    private String message;
    private T data;

    public static <T> ApiResponse<T> onSuccess(String message, T data) {
        return new ApiResponse<>(message, data);
    }
}