// com/_oormthon/goEuro/itinerary/repository/ItineraryDayRepository.java
package com._oormthon.goEuro.itinerary.repository;

import com._oormthon.goEuro.itinerary.domain.ItineraryDay;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ItineraryDayRepository extends JpaRepository<ItineraryDay, Long> {
    List<ItineraryDay> findAllByItineraryItineraryIdOrderByDayNumberAsc(Long itineraryId);
}
