package com._oormthon.goEuro.itinerary.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenerateItineraryResponse {
    private String title;
    private String city;
    private int days;
    private String currency;
    private List<DayPlan> plan;
    private List<String> tips;

    // DayPlan: record에서 class로 변경하고 필요한 어노테이션 추가
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DayPlan {
        private int day;
        private List<Stop> stops;
    }

    // Stop: record에서 class로 변경하고 필요한 어노테이션 추가
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Stop {
        private String name;
        private String category;
        private String time;
        private String address;
        private String note;
    }
}