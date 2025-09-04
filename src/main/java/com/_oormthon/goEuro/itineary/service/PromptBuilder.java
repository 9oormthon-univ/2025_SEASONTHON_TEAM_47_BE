package com._oormthon.goEuro.itineary.service;

import com._oormthon.goEuro.itineary.dto.GenerateItineraryRequest;
import org.springframework.stereotype.Component;

@Component // 스프링 빈
public class PromptBuilder {

    public String build(GenerateItineraryRequest req) {
        return """
        You are an expert Europe travel planner. Return ONLY JSON with this schema:
        {
          "city": string, "days": number, "currency": "EUR",
          "plan": [ { "day": number, "stops": [
            {"name": string, "category":"sight|food|cafe|museum|shopping|view",
             "time":"HH:mm-HH:mm", "address": string, "lat": number, "lng": number, "note": string}
          ] } ],
          "tips": [string]
        }
        Constraints:
        - City: %s, Days: %d, Month: %s, Interests: %s, Pace: %s, Budget: %s, Traveler: %s
        - Cluster nearby places to reduce backtracking. Include 1-2 hidden gems.
        - Return RAW JSON only. No markdown, no code fences.
        """.formatted(
                req.getCity(),
                req.getDays(),
                req.getMonth() == null ? "any" : req.getMonth(),
                req.getInterests() == null ? "[]" : req.getInterests().toString(),
                req.getPace(),
                req.getBudget(),
                req.getTravelerType()
        );
    }
}
