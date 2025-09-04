package com._oormthon.goEuro.common.config;

import lombok.Data;                                // 게터/세터 자동생성
import org.springframework.boot.context.properties.ConfigurationProperties; // yml 바인딩
import org.springframework.context.annotation.Configuration;                 // 빈 등록

@Data                                             // apiKey/model/baseUrl 필드의 게터/세터 생성
@Configuration                                    // 스프링 빈으로 등록
@ConfigurationProperties(prefix = "ai.gemini")    // .yml에서 해당 속성값 찾아옴
public class GeminiProperties {
    private String apiKey;                        // Gemini API 키(.env -> yml -> 여기)
    private String model;                         // 사용할 모델명
    private String baseUrl;                       // API 베이스 URL
}
