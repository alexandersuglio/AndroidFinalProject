package com.example.nomadfinal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nomadfinal.data.DailyWeather

import com.example.nomadfinal.data.Data

class MainViewModel : ViewModel()
{

    // weatherInfo variable is created to store the LiveData of Data class which has the
    // weather information for each location provided by the user.
    var weatherInfo = MutableLiveData<List<Data>>()

    // this function is created to get the live data of weatherInfo
    fun observeWeather(): LiveData<List<Data>>
    {
        return weatherInfo
    }

    // dailyWeatherData variable is created to store the LiveData of DailyWeather data class.
    // This has the 10-day weather forecast of each location provided by the user.
    var dailyWeatherData = MutableLiveData<List<DailyWeather>>()

    // this function is created to get the live data of dailyWeatherInfo
    fun observeDailyWeatherData () : LiveData<List<DailyWeather>>
    {
        return dailyWeatherData
    }

}