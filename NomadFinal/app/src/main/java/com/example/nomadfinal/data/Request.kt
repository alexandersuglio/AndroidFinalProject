package com.example.nomadfinal.data


import android.util.Log
import java.net.URL

// Request handles the network request based on the url and
// reads the text response and returns in a string format.

class Request (private val url: String) {

    fun run():String {
        val forecastJson = URL(url).readText()
        Log.d(javaClass.simpleName, forecastJson)
        return forecastJson
    }
}