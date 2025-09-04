package com._oormthon.goEuro.itineary.infra;

import com._oormthon.goEuro.common.config.GeminiProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Slf4j // Lombok이 자동으로 log 객체(sl4j Logger)를 만들어줌
@Component // 스프링 컴포넌트 등록(빈으로 관리됨)
@RequiredArgsConstructor // final 필드를 매개변수로 받는 생성자를 자동 생성
public class GeminiClient {

    private final WebClient geminiWebClient; // WebClient (HTTP 요청을 보낼 클라이언트)
    private final GeminiProperties props;    // application.yml의 Gemini 설정(키, 모델, URL)

    // 프롬프트를 받아 Gemini API에 요청을 보내고, 결과(JSON 텍스트)를 반환하는 메서드
    public Mono<String> generateContent(String prompt) {
        // Gemini API에 전달할 요청 바디(JSON 형식)
        var body = Map.of(
                "contents", List.of(Map.of(
                        "parts", List.of(Map.of("text", prompt)) // "text"에 프롬프트 넣기
                ))
        );

        // 호출할 API 경로: /models/{모델명}:generateContent?key={API키}
        String path = "/models/%s:generateContent?key=%s"
                .formatted(props.getModel(), props.getApiKey());

        // 디버그 로그: 어떤 모델을 사용하고, 프롬프트 길이가 몇 바이트인지 출력
        log.debug("[Gemini] request model={}, bytes={}", props.getModel(), prompt.length());

        // WebClient로 HTTP POST 요청 실행
        return geminiWebClient.post()
                .uri(path)            // 위에서 만든 경로
                .bodyValue(body)      // JSON 바디
                .retrieve()           // 응답 가져오기
                .onStatus(HttpStatusCode::isError, resp -> // 에러 상태 처리
                        resp.bodyToMono(String.class).flatMap(msg -> {
                            // 에러 로그 남기기
                            log.warn("[Gemini] error status={}, body={}", resp.statusCode(), msg);
                            // 예외로 던져서 상위에서 처리 가능하도록
                            return Mono.error(new RuntimeException("Gemini error: " + msg));
                        }))
                .bodyToMono(Map.class) // 응답을 Map으로 변환
                .map(this::extractText); // Map에서 실제 텍스트만 추출
    }

    // 응답 Map에서 텍스트를 꺼내는 메서드
    @SuppressWarnings("unchecked") // 제네릭 캐스팅 경고 무시
    private String extractText(Map<String, Object> resp) {
        // "candidates"라는 키에 후보 응답들이 들어 있음
        var candidates = (List<Map<String, Object>>) resp.get("candidates");
        if (candidates == null || candidates.isEmpty()) return ""; // 없으면 빈 문자열 반환

        // 첫 번째 후보(candidate)의 content 부분 가져오기
        var content = (Map<String, Object>) candidates.get(0).get("content");
        // content 안의 "parts" 배열 꺼내기
        var parts = (List<Map<String, Object>>) content.get("parts");
        // 첫 번째 part의 "text" 값을 반환 (실제 모델이 만들어준 텍스트)
        return (String) parts.get(0).get("text");
    }
}
