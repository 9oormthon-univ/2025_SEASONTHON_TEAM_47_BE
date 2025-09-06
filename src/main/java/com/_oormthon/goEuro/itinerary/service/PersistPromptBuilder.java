// com/_oormthon/goEuro/itinerary/service/PersistPromptBuilder.java
package com._oormthon.goEuro.itinerary.service;

import com._oormthon.goEuro.itinerary.dto.GenerateItineraryRequest;
import org.springframework.stereotype.Component;

@Component
public class PersistPromptBuilder {

    public String build(GenerateItineraryRequest req) {
        return """
        You are an expert Europe travel planner. Return ONLY RAW JSON (no markdown, no code fences):
        {
          "city": "%s", "days": %d, "currency": "EUR",
          "plan": [
            { "day": 1, "stops": [
              {"name":"string","category":"sight|food|cafe|museum|shopping|view",
               "start_time":"09:00","end_time":"10:30","address":"string","note":"string"}
            ]}
          ],
          "tips": ["string"]
        }
        Constraints:
        - Month: %s, Interests: %s, Pace: %s, Budget: %s, Traveler: %s
        - Cluster nearby places to reduce backtracking.
        - Include 1-2 hidden gems.
        - Output must strictly follow keys above. No extra fields.
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
