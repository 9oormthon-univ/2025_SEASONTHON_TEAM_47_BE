package com._oormthon.goEuro.itinerary.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItineraryStopListResponse {

    @JsonProperty("itinerary_id")
    private Long itineraryId;

    @JsonProperty("day_id")
    private Long dayId;

    @JsonProperty("stops")
    private List<Item> stops;

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Item {

        @JsonProperty("name")
        private String name;

        @JsonProperty("address")
        private String address;

        @JsonProperty("start_time")
        @JsonFormat(pattern = "HH:mm")
        private LocalTime startTime;

        @JsonProperty("end_time")
        @JsonFormat(pattern = "HH:mm")
        private LocalTime endTime;
    }
}