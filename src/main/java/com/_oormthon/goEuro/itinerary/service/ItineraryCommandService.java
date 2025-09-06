// com/_oormthon/goEuro/itinerary/service/ItineraryCommandService.java
package com._oormthon.goEuro.itinerary.service;

import com._oormthon.goEuro.itinerary.domain.Itinerary;
import com._oormthon.goEuro.itinerary.dto.ItineraryTitleUpdateRequest;
import com._oormthon.goEuro.itinerary.dto.ItineraryTitleUpdateResponse;
import com._oormthon.goEuro.itinerary.repository.ItineraryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ItineraryCommandService {

    private final ItineraryRepository itineraryRepository;

    public ItineraryCommandService(ItineraryRepository itineraryRepository) {
        this.itineraryRepository = itineraryRepository;
    }

    @Transactional
    public ItineraryTitleUpdateResponse updateTitle(Long itineraryId, ItineraryTitleUpdateRequest request) {
        Itinerary itinerary = itineraryRepository.findById(itineraryId)
                .orElseThrow(() -> new IllegalArgumentException("해당 여행 일정이 존재하지 않습니다. id=" + itineraryId));

        itinerary.setTitle(request.getTitle());
        itineraryRepository.save(itinerary);

        return new ItineraryTitleUpdateResponse(itinerary.getItineraryId(), itinerary.getTitle());
    }
}
