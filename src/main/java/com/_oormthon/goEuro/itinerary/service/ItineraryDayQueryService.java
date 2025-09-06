package com._oormthon.goEuro.itinerary.service;

import com._oormthon.goEuro.itinerary.domain.ItineraryDay;
import com._oormthon.goEuro.itinerary.dto.ItineraryDaySummaryResponse;
import com._oormthon.goEuro.itinerary.repository.ItineraryDayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItineraryDayQueryService {

    private final ItineraryDayRepository itineraryDayRepository;

    @Transactional(readOnly = true)
    public ItineraryDaySummaryResponse getDaySummaries(Long itineraryId) {
        var days = itineraryDayRepository
                .findAllByItineraryItineraryIdOrderByDayNumberAsc(itineraryId)
                .stream()
                .map(this::toItem)
                .collect(Collectors.toList());

        return ItineraryDaySummaryResponse.builder()
                .itineraryId(itineraryId)
                .days(days)
                .build();
    }

    private ItineraryDaySummaryResponse.Item toItem(ItineraryDay day) {
        return ItineraryDaySummaryResponse.Item.builder()
                .dayId(day.getDayId())
                .plannedDate(day.getPlannedDate())
                .build();
    }
}