package com._oormthon.goEuro.common.config;

import io.swagger.v3.oas.models.ExternalDocumentation; // 외부문서 정보
import io.swagger.v3.oas.models.OpenAPI;               // OpenAPI 루트
import io.swagger.v3.oas.models.info.Contact;          // 연락처
import io.swagger.v3.oas.models.info.Info;             // 기본 정보
import org.springframework.context.annotation.Bean;     // 빈 정의
import org.springframework.context.annotation.Configuration; // 설정

@Configuration                                          // 설정 클래스
public class OpenApiConfig {

    @Bean                                              // OpenAPI 스펙 빈
    public OpenAPI goEuroOpenAPI() {
        return new OpenAPI()                            // OpenAPI 객체 생성
                .info(new Info()                        // 기본 정보 설정
                        .title("goEuro API")            // API 제목
                        .description("AI 기반 유럽 여행 일정 생성 API") // 설명
                        .version("v1")                  // 버전
                        .contact(new Contact()          // 연락처 정보
                                .name("goEuro")
                                .email("support@goeuro.example")))
                .externalDocs(new ExternalDocumentation() // 외부 문서 링크(편의)
                        .description("Swagger UI")
                        .url("/swagger-ui"));           // UI 경로
    }
}
