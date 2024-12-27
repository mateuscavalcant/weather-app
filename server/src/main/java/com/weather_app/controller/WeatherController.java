package com.weather_app.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weather_app.service.WeatherService;

@RestController
@RequestMapping("/weather")
@CrossOrigin(origins = "https://weather-app-av1.vercel.app/")  // Permite CORS para o frontend
public class WeatherController {
    

    @Autowired
    private WeatherService weatherService;
    @PostMapping
    public ResponseEntity<?> getWeather(@RequestBody LocationRequest locationRequest) {
        try {
            Map<String, Object> weatherData = weatherService.getWeatherData(locationRequest.getLocation());
            if (weatherData == null || weatherData.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("weatherDataError", "No weather data from location was found"));
            }
            return ResponseEntity.ok(weatherData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("weatherDataError", e.getMessage()));
        }
    
}

    

    public static class LocationRequest {
        private String location;

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }
    }
}
