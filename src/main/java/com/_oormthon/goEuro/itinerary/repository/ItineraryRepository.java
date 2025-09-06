// com/_oormthon/goEuro/itinerary/repository/ItineraryRepository.java
package com._oormthon.goEuro.itinerary.repository;

import com._oormthon.goEuro.itinerary.domain.Itinerary;
import com._oormthon.goEuro.itinerary.dto.ItinerarySummaryView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItineraryRepository extends JpaRepository<Itinerary, Long> {

    // userId로 필터링 + 필요한 컬럼만 select (인터페이스 프로젝션)
    List<ItinerarySummaryView> findByUser_Id(Long userId);
}
