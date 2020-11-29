package com.example.nomadfinal

import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.nomadfinal.data.Data
import org.w3c.dom.Text

import android.widget.TextView

class WeatherPost : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.weather_row)

        var idk = intent.extras?.getStringArrayList("weatherData")


       // var idk3 = idk?.get(0)?.get(1)


    //    var idk3 = idk?.size


    //    var idk4 = idk?.toTypedArray()






     //   Log.d("weatherDataImport", idk.toString())

     //   var title = findViewById<TextView>(R.id.subRowHeading)

     //   var idk2 = idk?.toMutableList()




    //    Log.d("haha", "hey")




//        if (idk != null)
//        {
//
//            idk.get(0)
//
//
//            title.text = idk.get(0)
//        }


//        if (idk != null) {
//            Log.d("weatherDataImport2", idk.get(0).toString())
//        }


    }

}