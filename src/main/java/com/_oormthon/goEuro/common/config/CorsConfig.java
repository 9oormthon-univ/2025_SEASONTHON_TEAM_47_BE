package com._oormthon.goEuro.common.config;


import org.springframework.beans.factory.annotation.Value;           // yml 값 주입
import org.springframework.context.annotation.Bean;                 // 빈 정의
import org.springframework.context.annotation.Configuration;        // 설정
import org.springframework.web.servlet.config.annotation.CorsRegistry; // CORS 레지스트리
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer; // MVC 설정 인터페이스

@Configuration                                          // 설정 빈
public class CorsConfig {

    @Bean                                               // WebMvcConfigurer 빈 등록
    public WebMvcConfigurer corsConfigurer(
            @Value("${app.cors.origins}") String origins // 허용 도메인
    ) {
        return new WebMvcConfigurer() {                 // 익명 구현체 반환
            @Override
            public void addCorsMappings(CorsRegistry registry) { // CORS 매핑 추가
                registry.addMapping("/api/**")          // /api/** 경로에 대해서만
                        .allowedOrigins(origins.split(",")) // 허용 오리진(쉼표로 여러 개 가능)
                        .allowedMethods("GET","POST","PUT","PATCH","DELETE") // 허용 메서드
                        .allowCredentials(true);        // 쿠키 포함 허용 여부(필요시)
            }
        };
    }
}
