// com/_oormthon/goEuro/itinerary/domain/Itinerary.java
package com._oormthon.goEuro.itinerary.domain;

import com._oormthon.goEuro.common.entity.BaseTimeEntity;
import com._oormthon.goEuro.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "itinerary",
        indexes = { @Index(name = "idx_itinerary_user", columnList = "user_id") })
public class Itinerary extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "itinerary_id")
    private Long itineraryId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private Member user;

    @Column
    private String title;

    @Column
    private String startDate;

    @Column
    private String endDate;

    @Column(length = 60, nullable = false)
    private String city;

    @Column(nullable = false)
    private int days;

    @Column(length = 3, nullable = false)
    private String currency; // "EUR"
}
