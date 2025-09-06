package com._oormthon.goEuro.itinerary.controller;

import com._oormthon.goEuro.itinerary.dto.ItineraryStopListResponse;
import com._oormthon.goEuro.itinerary.service.ItineraryStopQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "ItineraryStop", description = "여행 일차별 스톱 조회 API")
@RestController
@RequestMapping("/api/itineraries")
@RequiredArgsConstructor
@Validated
public class ItineraryStopQueryController {

    private final ItineraryStopQueryService itineraryStopQueryService;

    @Operation(summary = "특정 일차의 스톱 목록 조회",
            description = "itinerary_id와 day_id로 start_time, end_time, name, address 목록을 반환합니다. sort_order 오름차순.")
    @GetMapping("/{itineraryId}/days/{dayId}/stops")
    public ResponseEntity<ItineraryStopListResponse> getDayStops(
            @PathVariable @Min(1) Long itineraryId,
            @PathVariable @Min(1) Long dayId
    ) {
        var response = itineraryStopQueryService.getStops(itineraryId, dayId);
        return ResponseEntity.ok(response);
    }
}