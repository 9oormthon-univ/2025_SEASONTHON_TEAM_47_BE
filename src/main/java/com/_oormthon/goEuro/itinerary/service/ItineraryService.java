package com._oormthon.goEuro.itinerary.service;

import com._oormthon.goEuro.itinerary.infra.GeminiClient;
import com._oormthon.goEuro.itinerary.dto.GenerateItineraryRequest;
import com._oormthon.goEuro.itinerary.dto.GenerateItineraryResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItineraryService {

    private final PromptBuilder promptBuilder; // 프롬프트 생성기
    private final GeminiClient geminiClient;   // Gemini API 연동
    private final ObjectMapper om = new ObjectMapper();

    public Mono<GenerateItineraryResponse> generate(GenerateItineraryRequest req) {
        String prompt = promptBuilder.build(req);
        log.debug("[Itinerary] generating for city={}, days={}", req.getCity(), req.getDays());

        return geminiClient.generateContent(prompt)
                .map(json -> {
                    try {
                        return om.readValue(json, GenerateItineraryResponse.class);
                    } catch (Exception e) {
                        log.warn("[Itinerary] invalid model JSON: {}", e.toString());
                        throw new ResponseStatusException(
                                HttpStatus.BAD_GATEWAY, "Invalid model response", e
                        );
                    }
                });
    }
}
