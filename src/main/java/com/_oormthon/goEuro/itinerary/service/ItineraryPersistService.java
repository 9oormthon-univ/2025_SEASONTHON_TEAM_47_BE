// com/_oormthon/goEuro/itinerary/service/ItineraryPersistService.java
package com._oormthon.goEuro.itinerary.service;

import com._oormthon.goEuro.itinerary.domain.*;
import com._oormthon.goEuro.itinerary.dto.AiItinerarySaveDTO;
import com._oormthon.goEuro.itinerary.dto.GenerateItineraryRequest;
import com._oormthon.goEuro.itinerary.infra.GeminiClient;
import com._oormthon.goEuro.itinerary.repository.*;
import com._oormthon.goEuro.member.entity.Member;
import com._oormthon.goEuro.member.repository.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDate;
import java.time.LocalTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItineraryPersistService {

    private final PersistPromptBuilder promptBuilder;        // 저장용 프롬프트
    private final GeminiClient geminiClient;                 // (패키지 경로 수정됨)
    private final ObjectMapper om = new ObjectMapper();

    private final MemberRepository memberRepository;
    private final ItineraryRepository itineraryRepository;
    private final ItineraryDayRepository itineraryDayRepository;
    private final ItineraryStopRepository itineraryStopRepository;

    public Mono<Long> generateAndSave(Long userId, String city, int days, LocalDate startDate,
                                      String month, java.util.List<String> interests,
                                      String pace, String budget, String travelerType) {

        // 기존 요청 DTO 재사용
        var req = new GenerateItineraryRequest();
        req.setCity(city);
        req.setDays(days);
        req.setMonth(month);
        req.setInterests(interests);
        req.setPace(pace);
        req.setBudget(budget);
        req.setTravelerType(travelerType);

        String prompt = promptBuilder.build(req);

        return geminiClient.generateContent(prompt)
                .map(json -> {
                    try {
                        return om.readValue(json, AiItinerarySaveDTO.class);
                    } catch (Exception e) {
                        throw new RuntimeException("Invalid model response", e);
                    }
                })
                // JPA는 블로킹이므로 별도 스레드풀에서 실행
                .flatMap(ai -> Mono.fromCallable(() -> saveTransactional(ai, userId, startDate))
                        .subscribeOn(Schedulers.boundedElastic()));
    }

    @Transactional
    protected Long saveTransactional(AiItinerarySaveDTO ai, Long userId, LocalDate startDate) {
        Member user = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        Itinerary it = itineraryRepository.save(
                Itinerary.builder()
                        .user(user)
                        .city(ai.city())
                        .days(ai.days())
                        .currency(ai.currency() == null ? "EUR" : ai.currency())
                        .build()
        );

        for (AiItinerarySaveDTO.DayPlan d : ai.plan()) {
            LocalDate date = (startDate != null) ? startDate.plusDays(d.day() - 1L) : LocalDate.now();

            ItineraryDay day = itineraryDayRepository.save(
                    ItineraryDay.builder()
                            .itinerary(it)
                            .dayNumber(d.day())
                            .plannedDate(date)
                            .build()
            );

            int order = 1;
            for (AiItinerarySaveDTO.Stop s : d.stops()) {
                itineraryStopRepository.save(
                        ItineraryStop.builder()
                                .itinerary(it)
                                .day(day)
                                .sortOrder(order++)
                                .name(s.name())
                                .categoryCode(s.category() == null ? "sight" : s.category())
                                .startTime(LocalTime.parse(s.start_time()))
                                .endTime(LocalTime.parse(s.end_time()))
                                .address(s.address())
                                .note(s.note())
                                .build()
                );
            }
        }
        return it.getItineraryId();
    }
}
