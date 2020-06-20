package com.example.myfirstapp;

import java.util.Date;

public class WeatherInfo {

    private String weather_state_name;
    private String weather_state_abbr;
    private String applicable_date;
    private Double min_temp;
    private Double max_temp;
    private Double wind_speed;
    private Integer predictability;


    public WeatherInfo(String weather_state_name, String weather_state_abbr, String applicable_date, Double min_temp, Double max_temp, Double wind_speed, Integer predictability) {
        this.weather_state_name = weather_state_name;
        this.weather_state_abbr = weather_state_abbr;
        this.applicable_date = applicable_date;
        this.min_temp = min_temp;
        this.max_temp = max_temp;
        this.wind_speed = wind_speed;
        this.predictability = predictability;
    }

    public WeatherInfo() {
        super();
    }


    //Getters And Setters
    public Double getMax_temp() {
        return max_temp;
    }

    public void setMax_temp(Double max_temp) {
        this.max_temp = max_temp;
    }

    public String getWeather_state_name() {
        return weather_state_name;
    }

    public void setWeather_state_name(String weather_state_name) {
        this.weather_state_name = weather_state_name;
    }

    public String getWeather_state_abbr() {
        return weather_state_abbr;
    }

    public void setWeather_state_abbr(String weather_state_abbr) {
        this.weather_state_abbr = weather_state_abbr;
    }

    public String getApplicable_date() {
        return applicable_date;
    }

    public void setApplicable_date(String applicable_date) {
        this.applicable_date = applicable_date;
    }

    public Double getMin_temp() {
        return min_temp;
    }

    public void setMin_temp(Double min_temp) {
        this.min_temp = min_temp;
    }

    public Double getWind_speed() {
        return wind_speed;
    }

    public void setWind_speed(Double wind_speed) {
        this.wind_speed = wind_speed;
    }

    public Integer getPredictability() {
        return predictability;
    }

    public void setPredictability(Integer predictability) {
        this.predictability = predictability;
    }
}
