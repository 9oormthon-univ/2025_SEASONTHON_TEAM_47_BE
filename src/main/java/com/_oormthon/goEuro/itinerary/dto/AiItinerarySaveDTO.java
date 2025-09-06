// com/_oormthon/goEuro/itinerary/dto/AiItinerarySaveDTO.java
package com._oormthon.goEuro.itinerary.dto;

import java.util.List;

public record AiItinerarySaveDTO(   // record는 불변 타입 final이랑 비슷
        String title,
        String city,
        int days,
        String currency,
        List<DayPlan> plan,
        List<String> tips
) {
    public record DayPlan(int day, List<Stop> stops) { }
    public record Stop(
            String name, String category,
            String start_time, String end_time,
            String address, String note
    ) { }
}
