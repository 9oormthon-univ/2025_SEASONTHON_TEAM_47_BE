// com/_oormthon/goEuro/itinerary/dto/ItinerarySummaryResponse.java
package com._oormthon.goEuro.itinerary.dto;

public record ItinerarySummaryResponse(
        String title,
        String startDate,
        String endDate
) {
    public static ItinerarySummaryResponse from(ItinerarySummaryView v) {
        return new ItinerarySummaryResponse(v.getTitle(), v.getStartDate(), v.getEndDate());
    }
}
