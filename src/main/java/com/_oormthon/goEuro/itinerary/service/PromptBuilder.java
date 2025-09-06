package com._oormthon.goEuro.itinerary.service;

import com._oormthon.goEuro.itinerary.dto.GenerateItineraryRequest;
import org.springframework.stereotype.Component;

@Component // 스프링 빈
public class PromptBuilder {

    public String build(GenerateItineraryRequest req) {
        return """
                You are a travel itinerary planning assistant. \s
                Based on the user’s input
                (arrival airport : %s,
                 departure airport : %s, 
                 travel dates : %s - %s
                 total number of travel days : %s, 
                 purpose of travel : %s, 
                 travel theme : %s, 
                 group type : %s, 
                 and optionally flight details : %s, 
                 booked accommodation : %s, 
                 and must-visit places : %s), create a recommended travel itinerary. \s
                
                The output must strictly follow this JSON format:
                
                {
                  "city": "string",        // main destination city
                  "days": number,          // total number of travel days
                  "currency": "string",    // local currency code (e.g., EUR, USD, KRW)
                  "plan": [
                    {
                      "day": number,       // day number in the itinerary (1-based)
                      "stops": [
                        {
                          "name": "string",      // attraction or place name
                          "category": "string",  // type (e.g., history, food, museum, shopping, view, cafe)
                          "time": "string",      // recommended visiting time (e.g., "09:00-11:00")
                          "address": "string",   // exact address or location
                          "note": "string"       // helpful tip or context for the traveler
                        }
                      ]
                    }
                  ]
                }
                
                Guidelines:
                - The itinerary must match the user’s inputs and preferences (purpose, theme, group type). \s
                - Ensure logical time allocation (e.g., morning/afternoon/evening slots). \s
                - Prioritize famous or highly recommended attractions. \s
                - If optional inputs (flight, hotel, must-visit places) are given, incorporate them naturally. \s
                - Keep the plan realistic and coherent for the total days. \s
                - Do not include any extra commentary or explanation outside of the JSON structure.
        """.formatted(
                req.getStart(),
                req.getEnd(),
                req.getStartDate(),
                req.getEndDate(),
                req.getDays(),
                req.getPurpose(),
                req.getTheme(),
                req.getParty(),
                nullToEmpty(req.getFlightInfo()),
                nullToEmpty(req.getAccommodationInfo()),
                nullToEmpty(req.getPreferInfo())
        );
    }
}
