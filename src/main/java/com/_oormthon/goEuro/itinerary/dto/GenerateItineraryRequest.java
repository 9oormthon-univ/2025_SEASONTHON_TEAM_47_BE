package com._oormthon.goEuro.itinerary.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data // getter/setter 자동 생성
public class GenerateItineraryRequest {
    @NotBlank(message = "출발지는 필수입니다.")
    private String start; // 출발 공항명

    @NotBlank(message = "도착지는 필수입니다.")
    private String end; // 출발 공항명

    @Min(value = 1, message = "여행 일수는 1일 이상이어야 합니다.")
    private int days; // 여행 일수

    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "출발일 형식은 yyyy-MM-dd 입니다.")
    private String startDate;

    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "도착일 형식은 yyyy-MM-dd 입니다.")
    private String endDate;

    private String purpose;        // 여행 목적
    private String theme;            // 여행 테마
    private String party;          // 여행 일행
    private String flightInfo;            // 항공편 정보
    private String accommodationInfo;       // 숙소 정보
    private String preferInfo;           // 가고 싶은 장소 정보
}
