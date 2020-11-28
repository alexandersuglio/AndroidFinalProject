package com.example.nomadfinal.data

data class Data(
    val location: String,
    val temperature: Int?,
    val weather: String,
    val icon: String,
    val time: String,
    val timeZone: String
)