// com/_oormthon/goEuro/itinerary/domain/ItineraryDay.java
package com._oormthon.goEuro.itinerary.domain;

import com._oormthon.goEuro.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "itinerary_day",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_itinerary_day_no", columnNames = {"itinerary_id","day_number"})
        },
        indexes = { @Index(name = "idx_day_itinerary", columnList = "itinerary_id") })
public class ItineraryDay extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "day_id")
    private Long dayId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "itinerary_id", nullable = false)
    private Itinerary itinerary;

    @Column(name = "day_number", nullable = false)
    private int dayNumber;  // 1..N

    @Column(name = "planned_date", nullable = false)
    private LocalDate plannedDate; // YYYY-MM-DD
}
