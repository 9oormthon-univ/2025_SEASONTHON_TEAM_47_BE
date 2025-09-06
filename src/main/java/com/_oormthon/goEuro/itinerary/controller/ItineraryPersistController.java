// com/_oormthon/goEuro/itinerary/controller/ItineraryPersistController.java
package com._oormthon.goEuro.itinerary.controller;

import com._oormthon.goEuro.itinerary.dto.GenerateItineraryRequest;
import com._oormthon.goEuro.itinerary.service.ItineraryPersistService;
import com._oormthon.goEuro.member.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Tag(name = "Itinerary", description = "일정 생성/저장 API")
@RestController
@RequestMapping("/api/itineraries")
@RequiredArgsConstructor
public class ItineraryPersistController {

    private final ItineraryPersistService persistService;

    @Operation(
            summary = "일정 생성+저장",
            description = "설문(출·도착, 날짜, 목적/테마/일행, 항공/숙소/선호)을 바탕으로 LLM 일정을 생성하고 ERD에 저장합니다. itinerary_id 반환"
    )
    @PostMapping(value = "/generate-and-save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ApiResponse<Long>> generateAndSave(
            @RequestParam("userId") Long userId,                     // 인증 연동 전 임시
            @Valid @RequestBody GenerateItineraryRequest req         // 본문 DTO 그대로 서비스로
    ) {
        return persistService.generateAndSave(userId, req)
                .map(id -> ApiResponse.onSuccess("saved", id));
    }
}
