package com._oormthon.goEuro.itinerary.service;

import com._oormthon.goEuro.itinerary.domain.ItineraryDay;
import com._oormthon.goEuro.itinerary.domain.ItineraryStop;
import com._oormthon.goEuro.itinerary.dto.ItineraryStopListResponse;
import com._oormthon.goEuro.itinerary.repository.ItineraryDayRepository;
import com._oormthon.goEuro.itinerary.repository.ItineraryStopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItineraryStopQueryService {

    private final ItineraryStopRepository itineraryStopRepository;
    private final ItineraryDayRepository itineraryDayRepository;

    @Transactional(readOnly = true)
    public ItineraryStopListResponse getStops(Long itineraryId, Long dayId) {
        // ✅ dayId가 해당 itinerary에 속하는지 가볍게 검증(안전)
        ItineraryDay day = itineraryDayRepository.findAllByItineraryItineraryIdOrderByDayNumberAsc(itineraryId)
                .stream()
                .filter(d -> d.getDayId().equals(dayId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "day_id does not belong to itinerary_id"
                ));

        var items = itineraryStopRepository
                .findAllByItineraryItineraryIdAndDayDayIdOrderBySortOrderAsc(itineraryId, dayId)
                .stream()
                .map(this::toItem)
                .collect(Collectors.toList());

        return ItineraryStopListResponse.builder()
                .itineraryId(itineraryId)
                .dayId(dayId)
                .stops(items)
                .build();
    }

    private ItineraryStopListResponse.Item toItem(ItineraryStop s) {
        return ItineraryStopListResponse.Item.builder()
                .name(s.getName())
                .address(s.getAddress())
                .startTime(s.getStartTime())
                .endTime(s.getEndTime())
                .build();
    }
}