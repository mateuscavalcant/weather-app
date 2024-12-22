package com.weather_app.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class WeatherService {

    @Value("${weather.api.key}")
    private String apiKey;

    public Map<String, Object> getWeatherData(String city) {
        String baseURL = "http://api.openweathermap.org/data/2.5/weather?q=";
        String url = String.format("%s%s&appid=%s", baseURL, city.trim(), apiKey);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response;

        try {
            response = restTemplate.getForEntity(url, String.class);
        } catch (Exception e) {
            throw new RuntimeException("Error sending the request: " + e.getMessage());
        }

        if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            throw new RuntimeException("Unauthorized request. Please check your API key.");
        }

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Location not found");
        }

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root;
        try {
            root = objectMapper.readTree(response.getBody());
        } catch (IOException e) {
            throw new RuntimeException("Error decoding JSON: " + e.getMessage());
        }

        JsonNode mainData = root.path("main");
        double temperatureValue = mainData.path("temp").asDouble();
        String celsiusTemp = String.format("%d°", (int) (temperatureValue - 273.15));

        JsonNode weatherDescription = root.path("weather").get(0);
        String description = weatherDescription.path("description").asText();
        String icon = weatherDescription.path("icon").asText();

        double humidity = mainData.path("humidity").asDouble();
        int cloudiness = root.path("clouds").path("all").asInt();
        double rainLastHour = root.path("rain").path("1h").asDouble(0.0);


        LocalDate today = LocalDate.now();
        DateTimeFormatter dayOfWeekFormatter = DateTimeFormatter.ofPattern("EEEE").withLocale(Locale.ENGLISH);
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMM").withLocale(Locale.ENGLISH);

        String dayOfWeek = today.format(dayOfWeekFormatter);
        int dayOfMonth = today.getDayOfMonth();
        String month = today.format(monthFormatter);

        Map<String, Object> weatherDataMap = new HashMap<>();
        weatherDataMap.put("city", city);
        weatherDataMap.put("temperature", celsiusTemp);
        weatherDataMap.put("description", description);
        weatherDataMap.put("icon", icon);
        weatherDataMap.put("humidity", humidity);
        weatherDataMap.put("cloudiness", cloudiness);
        weatherDataMap.put("rainLastHour", rainLastHour);
        weatherDataMap.put("dayOfWeek", dayOfWeek);
        weatherDataMap.put("dayOfMonth", dayOfMonth);
        weatherDataMap.put("month", month);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("weatherdata", weatherDataMap);

        return responseMap;
    }
}
