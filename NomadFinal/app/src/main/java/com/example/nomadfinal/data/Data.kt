package com.example.nomadfinal.data

import java.io.Serializable

// Data class to store the Weather information for all the locations, it also
// stores the 10-day Daily Weather of the data type DailyWeather which is also
// created for handling the 10-day forecast.

data class Data(val location: String, val temperature: Int?, val weather: String, val icon: String, val time: String, val timeZone: String, val dailyWeather: MutableList<DailyWeather>) : Serializable