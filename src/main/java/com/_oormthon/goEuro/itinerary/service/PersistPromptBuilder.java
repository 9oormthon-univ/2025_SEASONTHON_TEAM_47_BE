// com/_oormthon/goEuro/itinerary/service/PersistPromptBuilder.java
package com._oormthon.goEuro.itinerary.service;

import com._oormthon.goEuro.itinerary.dto.GenerateItineraryRequest;
import org.springframework.stereotype.Component;

@Component
public class PersistPromptBuilder {

    public String build(GenerateItineraryRequest req) {
        return """
        당신은 여행 일정 기획 어시스턴트입니다. \s
        다음 사용자 입력을 반영해 추천 여행 일정을 생성하세요:
        - 도착 공항: %s
        - 출발 공항: %s
        - 여행 날짜: %s - %s
        - 총 여행 일수: %s
        - 여행 목적: %s
        - 여행 테마: %s
        -同行(그룹) 유형: %s
        - (선택) 항공 정보: %s
        - 예약 숙소: %s
        - 꼭 가보고 싶은 장소: %s

        출력 요구사항(매우 중요):
        - 오직 **JSON 객체 1개만** 출력하세요.
        - 코드펜스(```), 백틱(`), 설명 문장, 주석(//, #, /* */) 등은 절대 포함하지 마세요.
        - 출력은 반드시 '{'로 시작해 '}'로 끝나야 합니다.
        - 모든 키/문자열은 큰따옴표(\")를 사용하고, 끝에 불필요한 쉼표(,)를 두지 마세요.
        - 스키마의 타입 예시는 **실제 값**으로 채우세요(예: "string" 대신 실제 문자열).

        JSON 스키마(예시 형태, 실제 값으로 채우기):
        {
          "title": "",                // 여행 컨셉에 맞는 제목 (문자열)
          "startDate":"",             // 여행 시작 날짜(문자열)
          "endDate":"",              // 여행 마지막 날짜(문자열)
          "city": "",                 // 주요 방문 도시 (문자열)
          "days": 0,                  // 총 여행 일수 (정수)
          "currency": "EUR",          // 통화 코드 (예: EUR, USD, KRW)
          "plan": [
            {
              "day": 1,              // 일정의 일차(1부터 시작)
              "stops": [
                {
                  "name": "",        // 장소/명소 이름
                  "category": "",    // 예: history, food, museum, shopping, view, cafe
                  "time": "",        // "HH:mm-HH:mm" 형식 (예: "09:00-11:00")
                  "address": "",     // 정확한 주소 또는 위치
                  "note": ""         // 팁/설명
                }
              ]
            }
          ],
          "tips": []                  // 선택: 간단 팁 목록 (문자열 배열)
        }

        작성 지침:
        - 사용자 입력과 선호(목적/테마/同行 유형)를 반영하세요.
        - 오전/오후/저녁으로 시간 배분이 논리적이어야 합니다.
        - 유명/평점 높은 명소를 우선하되 1~2개의 숨은 명소도 포함하세요.
        - 항공/숙소/필수 방문지가 제공되면 자연스럽게 동선에 반영하세요.
        - 총 일수에 맞게 현실적인 이동거리/체류시간으로 구성하세요.
        - 값이 불확실하면 빈 문자열("")이나 합리적 기본값을 넣되, **키는 제거하지 마세요**.
        - **JSON 외 어떤 텍스트도 출력하지 마세요.**
        """.formatted(
                req.getStart(),
                req.getEnd(),
                req.getStartDate(),
                req.getEndDate(),
                req.getDays(),
                req.getPurpose(),
                req.getTheme(),
                req.getParty(),
                req.getFlightInfo(),
                req.getAccommodationInfo(),
                req.getPreferInfo()
        );
    }
    private String toPromptValue(String value) {
        return (value == null || value.isBlank()) ? "정보 없음" : value;
    }
}
