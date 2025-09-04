package com._oormthon.goEuro.itineary.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data // getter/setter 자동 생성
public class GenerateItineraryRequest {
    @NotBlank(message = "도시는 필수입니다.")
    private String city; // 도시명

    @Min(value = 1, message = "여행 일수는 1일 이상이어야 합니다.")
    private int days; // 여행 일수

    private String month;           // 여행 월
    private List<String> interests; // 관심사
    private String pace;            // 속도
    private String budget;          // 예산
    private String travelerType;    // 여행자 유형
}
