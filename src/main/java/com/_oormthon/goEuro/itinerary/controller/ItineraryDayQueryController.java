package com._oormthon.goEuro.itinerary.controller;

import com._oormthon.goEuro.itinerary.dto.ItineraryDaySummaryResponse;
import com._oormthon.goEuro.itinerary.service.ItineraryDayQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "ItineraryDay", description = "여행 일차 요약 조회 API")
@RestController
@RequestMapping("/api/itineraries")
@RequiredArgsConstructor
@Validated
public class ItineraryDayQueryController {

    private final ItineraryDayQueryService itineraryDayQueryService;

    @Operation(summary = "일정 일차 요약 조회",
            description = "주어진 itinerary_id에 대한 ItineraryDay의 day_id와 planned_date 목록을 반환합니다.")
    @GetMapping("/{itineraryId}/days")
    public ResponseEntity<ItineraryDaySummaryResponse> getItineraryDaySummaries(
            @PathVariable("itineraryId") Long itineraryId
    ) {
        var response = itineraryDayQueryService.getDaySummaries(itineraryId);
        return ResponseEntity.ok(response);
    }
}