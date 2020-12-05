package com.example.nomadfinal.data

import java.io.Serializable

// Daily Weather is a data class created for handling the 10-day forecast.

data class DailyWeather(val minTemp: Int?, val maxTemp: Int?, val weather: String, val icon: String, val time: String, val location: String) : Serializable

