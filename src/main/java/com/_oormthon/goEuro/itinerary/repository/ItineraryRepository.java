// com/_oormthon/goEuro/itinerary/repository/ItineraryRepository.java
package com._oormthon.goEuro.itinerary.repository;

import com._oormthon.goEuro.itinerary.domain.Itinerary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItineraryRepository extends JpaRepository<Itinerary, Long> { }
