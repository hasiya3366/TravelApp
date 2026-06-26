package com.example.TravelApp.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class WeatherServiceTest {

    @Test
    void fallbackWeatherUsesDestinationNameAndCountry() {
        WeatherService service = new WeatherService();

        WeatherSummary summary = service.getFallbackWeather("Sigiriya", "Sri Lanka");

        assertEquals("Sigiriya", summary.getLocationName());
        assertEquals("Sri Lanka", summary.getCountryName());
        assertFalse(summary.getSummary().isBlank());
    }
}
