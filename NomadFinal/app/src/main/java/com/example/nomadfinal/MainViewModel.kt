package com.example.nomadfinal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nomadfinal.data.DailyWeather

import com.example.nomadfinal.data.Data

class MainViewModel : ViewModel()
{

    var weatherInfo = MutableLiveData<List<Data>>()

    fun observeWeather(): LiveData<List<Data>>
    {
        return weatherInfo
    }

    var dailyWeatherData = MutableLiveData<List<DailyWeather>>()

    fun observeDailyWeatherData () : LiveData<List<DailyWeather>>
    {
        return dailyWeatherData
    }

}