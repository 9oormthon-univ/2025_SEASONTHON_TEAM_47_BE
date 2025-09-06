// com/_oormthon/goEuro/itinerary/dto/ItineraryTitleUpdateRequest.java
package com._oormthon.goEuro.itinerary.dto;

import lombok.Getter;
import lombok.Setter;

public record ItineraryTitleUpdateRequest(
        @Getter
        @Setter
        String title
) {}
