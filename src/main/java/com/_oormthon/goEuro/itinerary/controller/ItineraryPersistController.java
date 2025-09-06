// com/_oormthon/goEuro/itinerary/controller/ItineraryPersistController.java
package com._oormthon.goEuro.itinerary.controller;

import com._oormthon.goEuro.member.dto.ApiResponse;
import com._oormthon.goEuro.itinerary.service.ItineraryPersistService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/itineraries")
@RequiredArgsConstructor
public class ItineraryPersistController {

    private final ItineraryPersistService persistService;

    @Operation(summary = "일정 생성+저장", description = "LLM으로 생성한 일정을 DB(ERD) 구조에 저장하고 itinerary_id 반환")
    @PostMapping("/generate-and-save")
    public Mono<ApiResponse<Long>> generateAndSave(@RequestBody SaveRequest req) {
        return persistService.generateAndSave(
                        req.getUserId(),
                        req.getCity(),
                        req.getDays(),
                        req.getStartDate(),
                        req.getMonth(),
                        req.getInterests(),
                        req.getPace(),
                        req.getBudget(),
                        req.getTravelerType()
                )
                .map(id -> ApiResponse.onSuccess("saved", id));
    }

    @Data
    public static class SaveRequest {
        @NotNull private Long userId;          // member.id
        @NotNull private String city;
        @NotNull private Integer days;
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        private LocalDate startDate;           // day1 기준일
        private String month;
        private List<String> interests;
        private String pace;
        private String budget;
        private String travelerType;
    }
}
