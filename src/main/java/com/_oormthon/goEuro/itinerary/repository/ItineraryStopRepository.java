// com/_oormthon/goEuro/itinerary/repository/ItineraryStopRepository.java
package com._oormthon.goEuro.itinerary.repository;

import com._oormthon.goEuro.itinerary.domain.ItineraryStop;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ItineraryStopRepository extends JpaRepository<ItineraryStop, Long> {
    List<ItineraryStop> findAllByItineraryItineraryIdOrderByDayDayIdAscSortOrderAsc(Long itineraryId);
}
