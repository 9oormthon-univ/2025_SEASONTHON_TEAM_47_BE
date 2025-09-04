package com._oormthon.goEuro.itineary.controller;

import com._oormthon.goEuro.itineary.dto.GenerateItineraryRequest;
import com._oormthon.goEuro.itineary.dto.GenerateItineraryResponse;
import com._oormthon.goEuro.itineary.service.ItineraryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Itinerary", description = "여행 일정 생성 API")
@RestController
@RequestMapping("/api/itineraries")
@RequiredArgsConstructor
public class ItineraryController {

    private final ItineraryService itineraryService; // 서비스 계층 주입

    @Operation(summary = "맞춤 일정 생성", description = "도시/일수/관심사 기반으로 일정 JSON을 생성합니다.")
    @PostMapping(value = "/generate", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<GenerateItineraryResponse> generate(@Valid @RequestBody GenerateItineraryRequest req) {
        return itineraryService.generate(req);
    }
}
