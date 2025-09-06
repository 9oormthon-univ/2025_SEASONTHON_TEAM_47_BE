// com/_oormthon/goEuro/itinerary/dto/ItineraryTitleUpdateResponse.java
package com._oormthon.goEuro.itinerary.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ItineraryTitleUpdateResponse {
    private Long itineraryId;
    private String updatedTitle;
}
