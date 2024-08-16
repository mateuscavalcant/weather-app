package com.weather_app.model;

public class WeatherData {
    private String temperature;
    private String city;
    private String description;
    private String icon;

    public WeatherData(String temperature, String city, String description, String icon) {
        this.temperature = temperature;
        this.city = city;
        this.description = description;
        this.icon = icon;
    }

    // Getters e Setters

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
