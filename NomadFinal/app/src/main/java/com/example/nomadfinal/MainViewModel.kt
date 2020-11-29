package com.example.nomadfinal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.example.nomadfinal.data.Data

class MainViewModel : ViewModel()
{

    var weatherInfo = MutableLiveData<List<Data>>()



    fun observeWeather(): LiveData<List<Data>>
    {
        return weatherInfo
    }



}