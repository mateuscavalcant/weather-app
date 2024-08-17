package com.weather_app.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.cdimascio.dotenv.Dotenv;

@Service
public class WeatherService {

    Dotenv dotenv = Dotenv.load();
    String apiKey = dotenv.get("API_KEY");

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
        String celsiusTemp = String.format("%d°C", (int) (temperatureValue - 273.15));

        JsonNode weatherDescription = root.path("weather").get(0);
        String description = weatherDescription.path("description").asText();
        String icon = weatherDescription.path("icon").asText();

        Map<String, Object> weatherDataMap = new HashMap<>();
        weatherDataMap.put("city", city);
        weatherDataMap.put("temperature", celsiusTemp);
        weatherDataMap.put("description", description);
        weatherDataMap.put("icon", icon);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("weatherdata", weatherDataMap);

        return responseMap;
    }
}
