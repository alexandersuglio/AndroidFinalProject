package com.example.nomadfinal.data

import java.io.Serializable


data class DailyWeather(val minTemp: Int?, val maxTemp: Int?, val weather: String, val icon: String, val time: String, val location: String) : Serializable

