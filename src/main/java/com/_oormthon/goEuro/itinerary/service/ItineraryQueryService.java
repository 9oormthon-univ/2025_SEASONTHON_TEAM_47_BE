// com/_oormthon/goEuro/itinerary/service/ItineraryQueryService.java
package com._oormthon.goEuro.itinerary.service;

import com._oormthon.goEuro.itinerary.dto.ItinerarySummaryResponse;
import com._oormthon.goEuro.itinerary.dto.ItinerarySummaryView;
import com._oormthon.goEuro.itinerary.repository.ItineraryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ItineraryQueryService {

    private final ItineraryRepository itineraryRepository;

    public ItineraryQueryService(ItineraryRepository itineraryRepository) {
        this.itineraryRepository = itineraryRepository;
    }

    @Transactional(readOnly = true)
    public List<ItinerarySummaryResponse> getSummariesByUserId(Long userId) {
        List<ItinerarySummaryView> views = itineraryRepository.findByUser_Id(userId);
        return views.stream().map(ItinerarySummaryResponse::from).toList();
    }
}
