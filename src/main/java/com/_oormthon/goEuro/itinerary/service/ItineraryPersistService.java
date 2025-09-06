// com/_oormthon/goEuro/itinerary/service/ItineraryPersistService.java
package com._oormthon.goEuro.itinerary.service;

import com._oormthon.goEuro.itinerary.domain.Itinerary;
import com._oormthon.goEuro.itinerary.domain.ItineraryDay;
import com._oormthon.goEuro.itinerary.domain.ItineraryStop;
import com._oormthon.goEuro.itinerary.dto.AiItinerarySaveDTO;
import com._oormthon.goEuro.itinerary.dto.GenerateItineraryRequest;
import com._oormthon.goEuro.itinerary.infra.GeminiClient;
import com._oormthon.goEuro.itinerary.repository.ItineraryDayRepository;
import com._oormthon.goEuro.itinerary.repository.ItineraryRepository;
import com._oormthon.goEuro.itinerary.repository.ItineraryStopRepository;
import com._oormthon.goEuro.member.entity.Member;
import com._oormthon.goEuro.member.repository.MemberRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItineraryPersistService {

    private final PersistPromptBuilder promptBuilder;   // 저장용 프롬프트(한글)
    private final GeminiClient geminiClient;
    private final ObjectMapper om = new ObjectMapper();

    private final MemberRepository memberRepository;
    private final ItineraryRepository itineraryRepository;
    private final ItineraryDayRepository itineraryDayRepository;
    private final ItineraryStopRepository itineraryStopRepository;

    /** 설문 요청을 받아 LLM 생성 → ERD 저장 → itinerary_id 반환 */
    public Mono<Long> generateAndSave(Long userId, GenerateItineraryRequest req) {
        String prompt = promptBuilder.build(req);

        // planned_date 기준 (요청에 없으면 오늘)
        LocalDate baseDate = (req.getStartDate() != null && !req.getStartDate().isBlank())
                ? LocalDate.parse(req.getStartDate())
                : LocalDate.now();

        return geminiClient.generateContent(prompt)
                .map(this::toSaveDto) // time -> start_time/end_time 보정
                .flatMap(ai ->
                        Mono.fromCallable(() -> saveTransactional(ai, userId, baseDate))
                                .subscribeOn(Schedulers.boundedElastic()) // JPA는 블로킹
                );
    }

    /** 모델 JSON을 내부 저장 DTO로 변환 (time 또는 start_time/end_time 모두 대응) */
    private AiItinerarySaveDTO toSaveDto(String json) {
        try {
            JsonNode root = om.readTree(json);
            String title = getText(root, "title", "여행 계획");
            String city = getText(root, "city", "unknown");
            int days   = getInt(root, "days", 1);
            String cur = getText(root, "currency", "EUR");

            List<AiItinerarySaveDTO.DayPlan> plans = new ArrayList<>();
            JsonNode planArr = root.get("plan");
            if (planArr != null && planArr.isArray()) {
                for (JsonNode dayNode : planArr) {
                    int dayNum = getInt(dayNode, "day", plans.size() + 1);

                    List<AiItinerarySaveDTO.Stop> stops = new ArrayList<>();
                    JsonNode stopsArr = dayNode.get("stops");
                    if (stopsArr != null && stopsArr.isArray()) {
                        for (JsonNode s : stopsArr) {
                            String name = getText(s, "name", "");
                            String cat  = getText(s, "category", "sight");
                            String addr = getText(s, "address", "");
                            String note = getText(s, "note", "");

                            // 우선 start_time/end_time 사용, 없으면 time 분해
                            String st = getText(s, "start_time", null);
                            String et = getText(s, "end_time", null);
                            if (st == null || et == null) {
                                String timeRange = getText(s, "time", "09:00-10:00");
                                String[] se = splitTimeRange(timeRange);
                                st = se[0]; et = se[1];
                            }

                            stops.add(new AiItinerarySaveDTO.Stop(
                                    name, cat, st, et, addr, note
                            ));
                        }
                    }
                    plans.add(new AiItinerarySaveDTO.DayPlan(dayNum, stops));
                }
            }

            // tips (있으면)
            List<String> tips = new ArrayList<>();
            JsonNode tipsArr = root.get("tips");
            if (tipsArr != null && tipsArr.isArray()) {
                tipsArr.forEach(t -> tips.add(t.asText("")));
            }

            return new AiItinerarySaveDTO(title, city, days, cur, plans, tips);
        } catch (Exception e) {
            log.warn("toSaveDto parse error: {}", e.toString());
            throw new RuntimeException("Invalid model response JSON", e);
        }
    }

    private static String getText(JsonNode node, String field, String dft) {
        JsonNode n = node.get(field);
        return (n == null || n.isNull()) ? dft : n.asText(dft);
    }
    private static int getInt(JsonNode node, String field, int dft) {
        JsonNode n = node.get(field);
        return (n == null || n.isNull()) ? dft : n.asInt(dft);
    }
    private static String[] splitTimeRange(String range) {
        String r = range == null ? "" : range.replace(" ", "");
        String[] parts = r.split("[-~]");
        String start = (parts.length > 0 && !parts[0].isBlank()) ? parts[0] : "09:00";
        String end   = (parts.length > 1 && !parts[1].isBlank()) ? parts[1] : "10:00";
        return new String[]{start, end};
    }

    @Transactional
    protected Long saveTransactional(AiItinerarySaveDTO ai, Long userId, LocalDate baseDate) {
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

        if (ai.plan() != null) {
            for (AiItinerarySaveDTO.DayPlan d : ai.plan()) {
                LocalDate date = baseDate.plusDays(d.day() - 1L);

                ItineraryDay day = itineraryDayRepository.save(
                        ItineraryDay.builder()
                                .itinerary(it)
                                .dayNumber(d.day())
                                .plannedDate(date)
                                .build()
                );

                int order = 1;
                if (d.stops() != null) {
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
            }
        }
        return it.getItineraryId();
    }
}
