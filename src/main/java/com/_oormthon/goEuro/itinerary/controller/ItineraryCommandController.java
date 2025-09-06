// com/_oormthon/goEuro/itinerary/controller/ItineraryCommandController.java
package com._oormthon.goEuro.itinerary.controller;

import com._oormthon.goEuro.itinerary.dto.ItineraryTitleUpdateRequest;
import com._oormthon.goEuro.itinerary.dto.ItineraryTitleUpdateResponse;
import com._oormthon.goEuro.itinerary.service.ItineraryCommandService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/itineraries")
public class ItineraryCommandController {

    private final ItineraryCommandService itineraryCommandService;

    public ItineraryCommandController(ItineraryCommandService itineraryCommandService) {
        this.itineraryCommandService = itineraryCommandService;
    }

    // 예: PATCH /api/itineraries/title/5
    @PatchMapping("/title/{itineraryId}")
    public ResponseEntity<ItineraryTitleUpdateResponse> updateTitle(
            @PathVariable Long itineraryId,
            @RequestBody ItineraryTitleUpdateRequest request
    ) {
        ItineraryTitleUpdateResponse response = itineraryCommandService.updateTitle(itineraryId, request);
        return ResponseEntity.ok(response); // 200 OK + 수정된 데이터 반환
    }
}
