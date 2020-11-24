//package edu.utap.samplefinal.data

package com.example.nomadfinal


import android.util.Log
import java.net.URL

class Request (private val url: String) {

    fun run():String {
        val forecastJson = URL(url).readText()
        Log.d(javaClass.simpleName, forecastJson)
        return forecastJson
    }
}