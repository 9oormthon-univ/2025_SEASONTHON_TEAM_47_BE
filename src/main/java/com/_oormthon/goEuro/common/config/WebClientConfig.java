package com._oormthon.goEuro.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration                                          // 설정 빈
public class WebClientConfig { // 다른 서버 api를 사용하기 위해 필요함

    @Bean("geminiWebClient")                             // 이름을 지정하여 주입 구분
    public WebClient geminiWebClient(GeminiProperties props) { // GeminiProperties 주입
        return WebClient.builder()                       // WebClient 빌더 시작
                .baseUrl(props.getBaseUrl())             // 기본 URL 설정
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE) // JSON 헤더
                .exchangeStrategies(ExchangeStrategies.builder() // 응답이 길 수 있으니
                        .codecs(c -> c.defaultCodecs().maxInMemorySize(8 * 1024 * 1024))   // 버퍼 8MB
                        .build())
                .build();                                // WebClient 생성
    }
}
