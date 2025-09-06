// com/_oormthon/goEuro/itinerary/dto/ItinerarySummaryView.java
package com._oormthon.goEuro.itinerary.dto;

public interface ItinerarySummaryView {
    String getTitle();
    String getStartDate(); // 현재 스키마가 VARCHAR 이므로 String 유지
    String getEndDate();
}
