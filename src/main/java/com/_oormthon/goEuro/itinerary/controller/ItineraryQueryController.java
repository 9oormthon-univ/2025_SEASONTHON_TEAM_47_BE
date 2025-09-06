// com/_oormthon/goEuro/itinerary/controller/ItineraryQueryController.java
package com._oormthon.goEuro.itinerary.controller;

import com._oormthon.goEuro.itinerary.dto.ItinerarySummaryResponse;
import com._oormthon.goEuro.itinerary.service.ItineraryQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ItineraryQueryController {

    private final ItineraryQueryService itineraryQueryService;

    public ItineraryQueryController(ItineraryQueryService itineraryQueryService) {
        this.itineraryQueryService = itineraryQueryService;
    }

    // 예: GET /api/users/1/itineraries/summaries
    @GetMapping("/users/itineraries/summaries/{userId}")
    public ResponseEntity<List<ItinerarySummaryResponse>> getItinerarySummaries(
            @PathVariable Long userId
    ) {
        List<ItinerarySummaryResponse> result = itineraryQueryService.getSummariesByUserId(userId);
        return ResponseEntity.ok(result); // 빈 배열 [] 로 응답 (404 아님)
    }
}
