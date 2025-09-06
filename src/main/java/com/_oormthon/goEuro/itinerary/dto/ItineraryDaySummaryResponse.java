package com._oormthon.goEuro.itinerary.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItineraryDaySummaryResponse {

    @JsonProperty("itinerary_id")
    private Long itineraryId;

    @JsonProperty("days")
    private List<Item> days;

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Item {

        @JsonProperty("day_id")
        private Long dayId;

        @JsonProperty("planned_date")
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate plannedDate;
    }
}