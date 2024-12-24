package com.weather_app.service;

import java.io.IOException;
import java.util.HashMap;
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

        // Dados do clima
        JsonNode mainData = root.path("main");
        double temperatureValue = mainData.path("temp").asDouble();
        double feelsLike = mainData.path("feels_like").asDouble();
        int feelsLikeCelsius = (int)Math.round(feelsLike - 273.15);
        int celsiusTemp = (int)Math.round(temperatureValue - 273.15);

        JsonNode weatherDescription = root.path("weather").get(0);
        String description = weatherDescription.path("description").asText();
        String icon = weatherDescription.path("icon").asText();

        JsonNode wind = root.path("wind");

        double speed = wind.path("speed").asDouble();

        double humidity = mainData.path("humidity").asDouble();
        double tempMin = mainData.path("temp_min").asDouble();
        int tempMinCelsius = (int)Math.round(tempMin - 273.15);
        double tempMax = mainData.path("temp_max").asDouble();
        int tempMaxCelsius = (int)Math.round(tempMax - 273.15);
        int cloudiness = root.path("clouds").path("all").asInt();
        double rainLastHour = root.path("rain").path("1h").asDouble(0.0);
        
        Map<String, Object> weatherDataMap = new HashMap<>();
        weatherDataMap.put("city", city);
        weatherDataMap.put("temperature", celsiusTemp);
        weatherDataMap.put("feelsLike", feelsLikeCelsius);
        weatherDataMap.put("tempMin", tempMinCelsius);
        weatherDataMap.put("tempMax", tempMaxCelsius);
        weatherDataMap.put("speed", speed);
        weatherDataMap.put("description", description);
        weatherDataMap.put("icon", icon);
        weatherDataMap.put("humidity", humidity);
        weatherDataMap.put("cloudiness", cloudiness);
        weatherDataMap.put("rainLastHour", rainLastHour);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("weatherdata", weatherDataMap);

        return responseMap;
    }

}

