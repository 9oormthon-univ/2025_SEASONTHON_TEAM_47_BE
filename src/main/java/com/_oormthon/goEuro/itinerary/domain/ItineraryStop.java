// com/_oormthon/goEuro/itinerary/domain/ItineraryStop.java
package com._oormthon.goEuro.itinerary.domain;

import com._oormthon.goEuro.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "itinerary_stop",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_stop_sort_in_day", columnNames = {"day_id","sort_order"})
        },
        indexes = {
                @Index(name = "idx_stop_day", columnList = "day_id"),
                @Index(name = "idx_stop_itinerary", columnList = "itinerary_id")
        })
public class ItineraryStop extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stop_id")
    private Long stopId;

    // 조회 최적화 위해 상위 PK 중복 보관(ERD에 있으면)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "itinerary_id", nullable = false)
    private Itinerary itinerary;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "day_id", nullable = false)
    private ItineraryDay day;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder; // 1..M

    @Column(length = 120, nullable = false)
    private String name;

    @Column(name = "category_code", length = 100, nullable = false)
    private String categoryCode; // sight|food|cafe|museum|shopping|view

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;  // HH:mm

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;    // HH:mm

    @Column(length = 200)
    private String address;

    @Column(length = 255)
    private String note;
}
