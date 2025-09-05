package com._oormthon.goEuro.member.dto;

import lombok.*;

public class AuthResponseDTO {
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor @AllArgsConstructor
    public static class LoginResponse {
        private Long id;
        private String name;
        private String email;
        private AuthTokens token;
    }

    @Getter @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthTokens {
        private String accessToken;
        private String refreshToken;
    }
}
